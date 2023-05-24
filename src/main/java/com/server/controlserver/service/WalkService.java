package com.server.controlserver.service;

import com.server.controlserver.domain.*;
import com.server.controlserver.dto.PingRequestDto;
import com.server.controlserver.dto.WalkRequestDto;
import com.server.controlserver.dto.WalkResponseDto;
import com.server.controlserver.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<Walk> findAllByPetId(Long petId){
        Pet pet = petRepository.findById(petId).get();
        return pet.getWalkList();
    }

    public Optional<Walk> findById( Long walkId){
        return walkRepository.findById(walkId);
    }

    // 가장 최근 산책로 가져오기
    public WalkResponseDto findByLastestWalk(Long petId) {
        Walk lastestWalk = walkRepository.latestWalkFindByPetId(petId).get();
        System.out.println("lastestWalk: " + lastestWalk);
        System.out.println("activity: " + lastestWalk.getActivity());
        System.out.println("roadMap: " + lastestWalk.getRoadMap());
        System.out.println("roeadMap_Pinglist: " + lastestWalk.getRoadMap().getPingList());
        System.out.println("startPing: " + lastestWalk.getStartPoint());
        System.out.println("endPing: " + lastestWalk.getEndPoint());
        System.out.println("walkedDate: " + lastestWalk.getWalkDate());


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

    public boolean findHotPlace(Long petId){
//        List<Walk> myWalk = walkRepository.findByPetId(petId);
//        List<Ping> mylist = new ArrayList<>();  // mylist를 ArrayList로 초기화합니다.
//        for(Walk w : myWalk) {
//            System.out.println("내 펫 산책: "+w.getPet().getId());
//            for (Ping p : w.getRoadMap().getPingList()) {
//                mylist.add(p);
//            }
//        }
//        System.out.println("핑 리스트: "+mylist); // 이 핑 리스트는 내 펫의 산책 핑 리스트

        List<Ping> allPingList = pingRepository.findAll();

        // 모든 Ping 객체를 반복하면서 가장 작은/큰 위도와 경도를 찾음
        for (Ping ping : allPingList) {
            double latitude = ping.getLatitude();
            double longitude = ping.getLongitude();

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

        // 구역을 100m * 100m 간격으로 나누어 탐색
        for (double lat = bottomLeft.getLatitude(); lat <= topRight.getLatitude(); lat += 0.001) {
            for (double lng = bottomLeft.getLongitude(); lng <= topRight.getLongitude(); lng += 0.001) {
                // 현재 좌표 (lat, lng)에 해당하는 구역 내에 속하는 Ping 객체 개수를 센다
                int subCount = 0;
                for (Ping ping : allPingList) {
                    double pingLat = ping.getLatitude();
                    double pingLng = ping.getLongitude();
                    if (pingLat >= lat && pingLat < lat + 0.001 && pingLng >= lng && pingLng < lng + 0.001) {
                        subCount++;
                    }
                }
                count += subCount;
                if(subCount > 0) {
                    System.out.println("구역 내 (" + lat + ", " + lng + ") 위치의 Ping 객체 개수: " + subCount);
                }
            }
        }

        System.out.println("구역 내 총 Ping 객체 개수: " + count);

        return true;
    }
}
