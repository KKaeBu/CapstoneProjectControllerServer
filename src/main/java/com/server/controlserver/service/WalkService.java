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
    public Board findHotSpot() {
        //코드 시작 시간
        double start = System.currentTimeMillis();

        // 모든 산책 리스트를 받아온다.
        List<Walk> allWalkList = walkRepository.findAll();

        // 각 펫 별 산책로를 저장 (key = petId, value = Coord 리스트)
        HashMap<String, List<Coord>> coordMap = new HashMap<>();
        // 각 산책로의 핑 리스트를 coord 리스트로 변환후 petId로 해쉬맵에 연결해서 저장
        setPingMap(allWalkList, coordMap);

        // =============== 메인 로직 ===============
        // 모든 핑 중 좌표 평면의 사각형 4개의 꼭짓점을 찾음 + 필요한 값도 추가
        HashMap<String, Object> rectVertex = findRectVertex(coordMap);

        double areaN = 0.001; // 핫스팟 지정 범위 areaN (0.001 = 100m)
        int areaN_int = 1000; // areaN이 정수가 될 수 있는 크기(각 좌표열 length에 곱해질값)

        /*
         * 4개의 꼭짓점 좌표와 핫스팟 범위 크기를 바탕으로하는 사각형의 바둑판(좌표평면)을 만든다.
         * 바둑판의 인덱스 순서는 아래와 같다. (크기는 유동적)
         * 16 17 18 19 20
         * 11 12 13 14 15
         * 6  7  8  9  10
         * 1  2  3  4  5
        * */
        Board board = makeBoard(rectVertex, areaN, areaN_int);

        // 모든 핑을 범위별로 나누어 보드의 해당하는 스팟에 넣음
        classifyAllPings(board, rectVertex, coordMap, areaN, areaN_int);

        // 전체 핑 base 핫스팟 찾기 (데이터 수가 적으면 얘로)
        // 전체 핑 중 머문시간이 긴 핑 base 핫스팟 찾기 (원래 이게 정석)
        // 각 강아지별 핫스팟 찾기 (만드는김에 추가용)

        // 코드 끝난 시간
        double end = System.currentTimeMillis();
        // 걸린 시간
        double duration = (end - start)/1000;

        System.out.println("코드 걸린 시간: " + duration);
        return board;
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

    /**
     * 공간 좌표를 좌표 평면상에 옮기면서
     * 가장 큰 좌표와 가장 작은 좌표를 기준으로 생기는
     * 사각형의 4 꼭짓점을 반환해준다.
     * 단, 이때 가장 작은 좌표는 소수점 둘째 자리에서 내림 (ex. 37.200000)
     * 가장 큰 좌표는 소수점 둘째 짜리에서 반올림 (ex. 37.500000)
     * */
    public HashMap<String, Object> findRectVertex(HashMap<String, List<Coord>> coordMap) {
        HashMap<String, Object> verTex = new HashMap<>(); // 4 꼭짓점의 좌표

        double minLat = Double.MAX_VALUE; // 가장 작은 latitude 값 (x좌표)
        double minLng = Double.MAX_VALUE; // 가장 작은 longitude 값 (y좌표)
        double maxLat = Double.MIN_VALUE; // 가장 큰 latitude 값 (x좌표)
        double maxLng = Double.MIN_VALUE; // 가장 큰 longitude 값 (y좌표)

        for(String key : coordMap.keySet()){
            for(Coord c : coordMap.get(key)){
                double latitude = c.getLatitude();
                double longitude =  c.getLongitude();

                // 가장 작은 위도(latitude)와 경도(longitude) 업데이트
                minLat = Math.min(minLat, latitude);
                minLng = Math.min(minLng, longitude);

                // 가장 큰 위도(latitude)와 경도(longitude) 업데이트
                maxLat = Math.max(maxLat, latitude);
                maxLng = Math.max(maxLng, longitude);

            }
        }

        // 최소, 최대 좌표 꼭짓점 값 가공(내림, 올림)
        minLat = Math.floor(minLat*10)/10.0; // 소수점 둘재짜리 내림
        minLng = Math.floor(minLng*10)/10.0; // 소수점 둘째자리 내림
        maxLat = Math.ceil(maxLat*10)/10.0; // 소수점 둘째자리 올림
        maxLng = Math.ceil(maxLng*10)/10.0; // 소수점 둘째자리 올림

        // 최소,최대 좌표값을 활용해 4 꼭짓점 좌표 객체 생성
        Coordinate minminCoord = new Coordinate(minLat, minLng); // (0,0)
        Coordinate maxminCoord = new Coordinate(maxLat, minLng); // (n,0)
        Coordinate maxmaxCoord = new Coordinate(maxLat, maxLng); // (n,n)
        Coordinate minmaxCoord = new Coordinate(minLat, maxLng); // (0,n)

        // latitude, longitude 범위 크기 구하기
        int latLength = (int)Math.round((maxLat*10 - minLat*10));
        int lngLength = (int)Math.round((maxLng*10 - minLng*10));
        int rectArea = latLength * lngLength;

        // 생성된 꼭짓점 좌표 객체 및 필요 값들을 해쉬맵에 넣기
        verTex.put("minminCoord", minminCoord);
        verTex.put("maxminCoord", maxminCoord);
        verTex.put("maxmaxCoord", maxmaxCoord);
        verTex.put("minmaxCoord", minmaxCoord);
        verTex.put("minLat", minLat);
        verTex.put("minLng", minLng);
        verTex.put("maxLat", maxLat);
        verTex.put("maxLng", maxLng);
        verTex.put("latLnegth_double", maxLat - minLat);
        verTex.put("lngLength_double", maxLng - minLng);
        verTex.put("latLength_int", latLength);
        verTex.put("lngLength_int", lngLength);
        verTex.put("rectArea", rectArea);

        return verTex;
    }

    /**
     * 좌표 꼭지점으로 계산한 사각형의 크기를 바탕으로 바둑판(보드)를 만들어서 반환해줌
     * */
    public Board makeBoard(HashMap<String, Object> rectVertex, double areaN, int areaN_int) {
        double width = Double.parseDouble(rectVertex.get("latLnegth_double").toString()); // 보드의 가로 길이
        double height = Double.parseDouble(rectVertex.get("lngLength_double").toString()); // 보드의 세로 길이
        int boardSize = Integer.parseInt(rectVertex.get("rectArea").toString()) * (int)Math.pow(areaN_int, 2); // 바둑판에 있는 공간의 총 개수

        return new Board(
                boardSize,
                width*areaN,
                height*areaN,
                areaN,
                new HashMap<>()
        );
    }

    /**
     * 모든 핑을 보드의 각 스팟에 분류함
     * */
    public void classifyAllPings(Board board, HashMap<String, Object> rectVertex, HashMap<String, List<Coord>> coordMap, double areaN, int areaN_int) {
        Collection<List<Coord>> list_coordList = coordMap.values(); // coordMap에서 모든 value값만 빼옴(즉, coordList 들만 빼옴)
        List<Coord> allCoordList = new ArrayList<>(); // 모든 coord 리스트

        // coordList들을 하나의 리스트에 담음
        for(List<Coord> cl : list_coordList) {
            allCoordList.addAll(cl);
        }

        /* !!주의 할 점!!
        *  double, float 등 소수를 프로그래밍단에서 사칙연산 시행시
        *  그 값이 제대로 나오지 않는 경우가 드물다
        *  이를 해결하려면 BigDecimal을 써야함
        *  하지만, 그러러면 매 계산시마다 각 값들을 변환해줘야함
        *  그렇기에 여기서 범위 및 좌표 계산시에는 double 값들을
        *  정수형으로 바꿔서 계산
        *  */

        // 사용되는 값들을 소수형 -> 정수형 변환
        int minLat = (int)(Double.parseDouble(rectVertex.get("minLat").toString()) * 1000000);
        int maxLat = (int)(Double.parseDouble(rectVertex.get("maxLat").toString()) * 1000000);
        int minLng = (int)(Double.parseDouble(rectVertex.get("minLng").toString()) * 1000000);
        int maxLng = (int)(Double.parseDouble(rectVertex.get("maxLng").toString()) * 1000000);
        HashMap<String, Spot> spotList = board.getSpotList();

        // 각 핑들을 보드 내의 해당하는 스팟에 집어넣음
        for(Coord c : allCoordList) {
            /* 어차피 해당 좌표들이 어떤 스팟에 들어가는지만
            * 찾으면 되기에 각 좌표 값에 해당하는 최소 범위값을 찾음
            * 스팟의 범위는 spotSize 값과 같으므로
            * 해당 값을 기준으로 범위를 나눔
            * */
            int minRangeOfLat = (int)Math.floor(c.getLatitude()*areaN_int) * (1000000/areaN_int);
            int minRangeOfLng = (int)Math.floor(c.getLongitude()*areaN_int) * (1000000/areaN_int);
            
            /* 해당 좌표가 기준위치(minLat, minLng)로 부터
            * 스팟의 범위만큼 얼마나 떨어져 있는지를 판계산
            * (이것이 인덱스의 위치 계산)
            * */
            int lat_spot_num = (maxLat - minLat) / areaN_int; // x축의 spot 개수
            int lat_index = (minRangeOfLat - minLat) / areaN_int; // 좌표의 x 축에서의 index 위치
            int lng_index = (minRangeOfLng - minLng) / areaN_int; // 좌표의 y 축에서의 index 위치
            int spot_index = lat_index + (lng_index * lat_spot_num); // 좌표가 들어갈 spot의 번호

            // 보드내의 spot_index에 해당하는 스팟에 좌표를 넣어줌
            // (Key: spot_index, Value: Coord)
            if(!spotList.containsKey(Integer.toString(spot_index))){ // 보드판에서 해당 spot이 빈 스팟일때
                // 스팟 생성
                Spot spot = new Spot(
                        spot_index,
                        areaN,
                        areaN,
                        Math.floor(c.getLatitude()*areaN_int) / (double)areaN_int,
                        Math.ceil(c.getLatitude()*areaN_int) / (double)areaN_int,
                        Math.floor(c.getLongitude()*areaN_int) / (double)areaN_int,
                        Math.ceil(c.getLongitude()*areaN_int) / (double)areaN_int,
                        new ArrayList<>()
                        );
                // 스팟의 좌표 리스트에 해당 좌표 추가
                spot.getCoordList().add(c);
                // 만들어진 스팟을 보드판의 위치하는 인덱스에 붙임
                spotList.put(Integer.toString(spot_index), spot);
            }else{ // 보드판에 해당 스팟이 이미 있을때
                Spot spot = spotList.get(Integer.toString(spot_index));
                spot.getCoordList().add(c);
            }
        }

        // 보드판출력 확인용
        HashMap<String, Spot> h = board.getSpotList();
        for(Spot s : h.values()){
            System.out.println("spotNum: " + s.getNumber() + ", 좌표 개수: " + s.getCoordList().size());
        }

    }
}
