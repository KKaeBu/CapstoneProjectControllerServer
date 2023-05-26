package com.server.controlserver.service;

import com.server.controlserver.domain.*;
import com.server.controlserver.dto.PetResponseDto;
import com.server.controlserver.dto.PingRequestDto;
import com.server.controlserver.dto.WalkRequestDto;
import com.server.controlserver.dto.WalkResponseDto;
import com.server.controlserver.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class WalkService {
    /*
        ************ 현재 인기있는 구역을 찾아내는 로직
        1. 모든 반려동물의 산책로를 가져온다. or 모든 산책의 Ping을 가져온다. 어떤 Pet의 Ping인지 구별하려면 Walk를 통해서 Ping들을 불러와야할 것 같음
        2. 단, Ping을 가져올 때 어떤 Pet의 Ping인지는 구별해야함.
        -> 사용자 본인의 Ping을 인기있는 구역을 판단할 때 지표로써 사용되면 안되기 때문에.
        ex) 인기있는 구역을 판단하는 범위가 100m라고 했을 때, 한 사용자가 100m 안에서 많은 핑을 발생시켰다면,
            해당구역은 한 사용자만이 사용했음에도, 인기있는 구역으로 판단 할 수 있기 때문에, 본인의 핑은 제외시켜야한다.
        3. 모든 Ping을 기준으로 우리가 설정한 범위(위도,경도에서 +- 값으로 주면 될거같음)내에 본인을 제외한 사용자의 Ping의 개수로 인기있는 구역을 판단
     */

    private final WalkRepository walkRepository;
    private final PingRepository pingRepository;
    private final ActivityRepository activityRepository;
    private RoadMapRepository roadMapRepository;
    private final PetRepository petRepository;

    // 가장 작은 위도(latitude)와 경도(longitude)를 초기값으로 설정
    double minLatitude = Double.MAX_VALUE;
    double minLongitude = Double.MAX_VALUE;

    // 가장 큰 위도(latitude)와 경도(longitude)를 초기값으로 설정
    double maxLatitude = Double.MIN_VALUE;
    double maxLongitude = Double.MIN_VALUE;

    @Autowired
    public WalkService(WalkRepository walkRepository, PingRepository pingRepository, ActivityRepository activityRepository, RoadMapRepository roadMapRepository, PetRepository petRepository) {
        this.walkRepository = walkRepository;
        this.pingRepository = pingRepository;
        this.activityRepository = activityRepository;
        this.roadMapRepository = roadMapRepository;
        this.petRepository = petRepository;
    }

    public WalkResponseDto walkOver(WalkRequestDto walkRequestDto, String key, ConcurrentHashMap<String, List<PingRequestDto>> pingList, Long petId) {
        //Activity 저장
        Activity activity = walkRequestDto.toActivityEntity();
        activityRepository.save(activity);

        // pingList & RoadMap 저장
        List<Ping> pl = new ArrayList<>();
        RoadMap roadMapResult = null;
        for (String k : pingList.keySet()){
            List<PingRequestDto> pingReqList = pingList.get(k);
            if(pingReqList != null){
                for (PingRequestDto pingReq : pingReqList){
                    Ping ping = pingReq.toPingEntity();
                    pl.add(ping);
                }
                RoadMap roadMap = walkRequestDto.toRoadMapEntity();
                roadMapResult = pingRepository.saveRoadMapPingList(roadMap, pl);
            }

            // 저장이 완료되면, ConcurrentHashMap에서 해당 데이터를 삭제
            pingList.remove(k);
        }

        //Walk 저장
        Ping startPoint = pl.get(0);
        Ping endPoint = pl.get(pl.size()-1);
        Walk walk = walkRequestDto.toWalkEntity(startPoint, endPoint, roadMapResult, activity);

        //Pet 받아오기
        Pet pet = petRepository.findById(petId).get();

        //결과 받기
        Walk result = walkRepository.save(walk, pet);

        //데이터 할당 청소
        pl.clear();

        WalkResponseDto walkResponseDto = new WalkResponseDto(
                result.getStartPoint(),
                result.getEndPoint(),
                result.getRoadMap(),
                result.getActivity(),
                result.getWalkDate()
        );


        return walkResponseDto;
    }

    public List<WalkResponseDto> findAllByPetId(Long petId){
        List<Walk> walkList = walkRepository.findAllByPetId(petId);
        List<WalkResponseDto> wrdList = new ArrayList<>();

        for(Walk w : walkList) {
            WalkResponseDto wrd = new WalkResponseDto(
                    w.getStartPoint(),
                    w.getEndPoint(),
                    w.getRoadMap(),
                    w.getActivity(),
                    w.getWalkDate()
            );
            wrdList.add(wrd);
        }

        return wrdList;
    }

    public Optional<Walk> findById( Long walkId){
        return walkRepository.findById(walkId);
    }

    /** 가장 최근 산책로 가져오기 */
    public WalkResponseDto findByLastestWalk(Long petId) {
        Walk lastestWalk = walkRepository.lastestWalkFindByPetId(petId).get();

        return new WalkResponseDto(
                lastestWalk.getStartPoint(),
                lastestWalk.getEndPoint(),
                lastestWalk.getRoadMap(),
                lastestWalk.getActivity(),
                lastestWalk.getWalkDate()
        );
    }

    public RoadMap findRoadMapById(Long walkId) { return walkRepository.findById(walkId).get().getRoadMap();}

    public boolean deleteAll(Long petId){
        Pet pet = petRepository.findById(petId).get();
        if (pet.getId() != null){
            pet.setWalkList(null);
            petRepository.update(pet);
            return true;
        }else{
            return false;
        }
    }

    public boolean deleteWalk(Long walkId){
        Walk walk = walkRepository.findById(walkId).get();
        if(walk.getId()!=null){
            walkRepository.delete(walk);
            return true;
        }else{
            return false;
        }
    }


    public String getCoordsList(Long walkId) {
        List<Ping> pl = walkRepository.coordsListFindByWalkId(walkId);

        // 받아온 산책 핑 리스트 처리 과정이 필요 (리턴값도 변경해야함)

        return pl.toString();
    }

    public List<Coordinate> findHotPlace(){
        List<Ping> allPingList = pingRepository.findAll();

        // 모든 Ping 객체를 반복하면서 가장 작은/큰 위도와 경도를 찾음
        for (Ping ping : allPingList) {
            if(ping.getAltitude() == 5) //에뮬레이터 위치 배제
                continue;

            double latitude = coordFloor6(ping.getLatitude());
            double longitude =  coordFloor6(ping.getLongitude());

            // 가장 작은 위도(latitude)와 경도(longitude) 업데이트
            minLatitude = Math.min(minLatitude, latitude);
            minLongitude = Math.min(minLongitude, longitude);

            // 가장 큰 위도(latitude)와 경도(longitude) 업데이트
            maxLatitude = Math.max(maxLatitude, latitude);
            maxLongitude = Math.max(maxLongitude, longitude);
        }

        System.out.println("minLatitude : " + minLatitude);
        System.out.println("minLongitude : " + minLongitude);
        System.out.println("maxLatitude : " + maxLatitude);
        System.out.println("maxLongitude : " + maxLongitude);

        // 구역의 왼쪽 아래 꼭지점
        Coordinate bottomLeft = new Coordinate(minLatitude, minLongitude);

        // 구역의 오른쪽 위 꼭지점
        Coordinate topRight = new Coordinate(maxLatitude, maxLongitude);

        // 구역 내의 100m 간격으로 Ping 객체의 개수를 세는 변수
        int count = 0;

        // 인기 구역 지정 범위 areaN (0.001 = 100m)
        double areaN = 0.0005;

        // 인기 구역의 위도, 경도를 저장할 빈 List
        List<Coordinate> hotPlace = new ArrayList<>();

        // 구역을 100m * 100m 간격으로 나누어 탐색
        for (double lat = bottomLeft.getLatitude(); lat <= topRight.getLatitude(); lat += areaN) {
            for (double lng = bottomLeft.getLongitude(); lng <= topRight.getLongitude(); lng += areaN) {
                // 현재 좌표 (lat, lng)에 해당하는 구역 내에 속하는 Ping 객체 개수를 센다
                int subCount = 0;
                for (Ping ping : allPingList) {
                    double pingLat = ping.getLatitude();
                    double pingLng = ping.getLongitude();
                    if (pingLat >= lat && pingLat < lat + areaN && pingLng >= lng && pingLng < lng + areaN) {
                        subCount++;
                    }
                }
                count += subCount;
                if(subCount > 0) {
                    System.out.println("구역 내 (" + coordFloor6(lat) + ", " + coordFloor6(lng) + ") 위치의 Ping 객체 개수: " + subCount);
                }
                if(subCount > 10){
                    Coordinate hotPing = new Coordinate(coordFloor6(lat),coordFloor6(lng));
                    hotPlace.add(hotPing);
                }
            }
        }

        System.out.println("구역 내 총 Ping 객체 개수: " + count);
        System.out.println("Ping이 10개 이상 밀집된 구역 : " + hotPlace);
        // hotPlace리스트의 각 객체에서 위도,경도 + 0.001 (100m)를 더한 범위를 말한다.
        return hotPlace;
    }

    /**
    핫 스팟 좌표리스트를 반환해주는 함수 (hotplace랑 같은 기능)
    근데 이건 머문 시간 계산
     */
    public HashMap<String, List<Coord>> findHotSpot() {
        //코드 시작 시간
        double start = System.currentTimeMillis();

        // 모든 산책 리스트를 받아온다.
        List<Walk> allWalkList = walkRepository.findAll();

        // 각 펫 별 산책로를 저장 (key = petId, value = Coord 리스트)
        HashMap<String, List<Coord>> coordMap = new HashMap<>();
        // 각 산책로의 핑 리스트를 coord 리스트로 변환후 petId로 해쉬맵에 연결해서 저장
        setPingMap(allWalkList, coordMap);

        // 메인 로직



        // 코드 끝난 시간
        double end = System.currentTimeMillis();
        // 걸린 시간
        double duration = (end - start)/1000;

        System.out.println("코드 걸린 시간: " + duration);

        return coordMap;
    }




    /**
    모든 산책리스트에서 산책로안에있는 pingList만 뽑아서
    해당되는 pet의 petId와 연결하여 HashMap에 저장
    (key: petId, value: List<Ping>)
     */
    private void setPingMap(List<Walk> allWalkList, HashMap<String, List<Coord>> coordMap) {
        //모든 산책 리스트에서 Ping의 개수가 적은걸 걸름 (에뮬레이터때문에)
        for(Walk w : allWalkList) {
            // 에뮬레이터 데이터는 빼버림1
            if(w.getStartPoint().getId() == w.getEndPoint().getId())
                continue;

            // 정리된 산책 리스트에서 각 산책의 petId
            // 즉, 각자 다른 강아지들을 산책로 리스트의 index 값으로 쓰고
            // 각각의 value에는 coord 리스트를 추가한다.
            String petId = w.getPet().getId().toString();
            // 각 산책의 펫 데이터 (사용자에게 반환해줄거기에 ResponseDto 사용)
            PetResponseDto pet = new PetResponseDto(
                    w.getPet().getId(),
                    w.getPet().getName(),
                    w.getPet().getAge(),
                    w.getPet().getSex(),
                    w.getPet().getWeight(),
                    w.getPet().getIsNeutered(),
                    w.getPet().getSpecies()
            );


            List<Ping> pingList = w.getRoadMap().getPingList(); // 해당 산책의 산책로의 핑리스트
            List<Coord> coordList = new ArrayList<>(); // 핑 리스트를 coord로 변환후 저장할 coord 리스트

            for(int i = 0; i < pingList.size(); i++) {
                Coord coord;
                Ping curPing = pingList.get(i);

                //에뮬레이터 데이터 거르기2
                if(curPing.getAltitude() == Integer.valueOf(5).doubleValue())
                    continue;

                if(i == 0) {
                    // 첫번째 핑의 경우 시작 지점이므로 해당 핑에서 머문 시간은 0초
                    coord = new Coord(coordFloor6(
                            curPing.getLatitude()),
                            coordFloor6(curPing.getLongitude()),
                            Integer.valueOf(0).longValue(),
                            pet
                    );
                }else{
                    coord = new Coord(
                            coordFloor6(curPing.getLatitude()),
                            coordFloor6(curPing.getLongitude()),
                            getCalcStayTime(pingList.get(i-1).getCreateTime(), curPing.getCreateTime()),
                            pet
                    );
                }
                coordList.add(coord);
            }

            if(!coordMap.containsKey(petId)){ // 해당 키가 해쉬맵에 없다면 (처음 넣는 경우)
                if(!coordList.isEmpty()) //에뮬 데이터 거르기3
                    coordMap.put(petId, coordList);
            }else{ // 해당 키가 이미 있을 경우
                // addAll은 각 요소를 해당 배열의 뒤에 이어서 넣어줌
                coordMap.get(petId).addAll(coordList);
            }
        }
    }

    /** 위도, 경도, 고도 등의 값을 소수점 6자리까지만 나타내도록 잘라주는 함수 */
    public double coordFloor6(double coord) {
        double floorCoord = Math.floor(coord*1000000) / 1000000.0;

        // 소수점 계산은 정확하게 안나오기 때문에
        // BigDecimal을 사용함
        BigDecimal result = new BigDecimal(String.valueOf(floorCoord));
        BigDecimal correctionValue = new BigDecimal(String.valueOf(0.000001)); // 보정값

        // 소수점 자리수가 6자리보다 적으면 보정값을 더해줌
        if(result.scale() < 6)
            return result.add(correctionValue).doubleValue(); //이런다고 원본 값이 바뀌진 않음 그래서 바로 리턴해줘야댐

        // BigDecimal에서 double로 값을 꺼낼땐 doubleValue()를 사용
        return result.doubleValue();

    }

    /** 두 날짜 사이의 시간차를 계산하여 이를 초 단위로 반환 */
    public Long getCalcStayTime(Date preDate, Date curDate) {
        // Date 타입의 두 날짜를 LocalDateTime으로 변경
        LocalDateTime preLocalDateTime1 = preDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime curLocalDateTime2 = curDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        // Duration을 사용해 두 날짜 사이의 시간차를 계산
        Duration duration = Duration.between(preLocalDateTime1, curLocalDateTime2);

        // Duration의 getSeconds를 사용해 두 날짜사이의 시간차를 로 반환
        Long seconds = duration.getSeconds() % 60;

        return seconds;
    }
}
