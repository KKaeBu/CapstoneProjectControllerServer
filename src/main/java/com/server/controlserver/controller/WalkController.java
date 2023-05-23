package com.server.controlserver.controller;

import com.server.controlserver.domain.RoadMap;
import com.server.controlserver.domain.Walk;
import com.server.controlserver.dto.PingRequestDto;
import com.server.controlserver.dto.WalkRequestDto;
import com.server.controlserver.service.WalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WalkController {

    private final WalkService walkService;
    private final ConcurrentHashMap<String, List<PingRequestDto>> pingList;

    @Autowired
    public WalkController(WalkService walkService) {
        this.walkService = walkService;
        this.pingList = new ConcurrentHashMap<String, List<PingRequestDto>>();
    }

    @PostMapping("/api/pets/{petId}/walks")
    @ResponseBody
    public ResponseEntity<Walk> walkEnd(@PathVariable Long petId, @RequestBody WalkRequestDto walkRequestDto) {
        System.out.println("petId: " + petId);
        System.out.println("walkRequest: " + walkRequestDto);
        System.out.println("PingList: " + walkRequestDto.getPingList());
        List<PingRequestDto> reqPingList = walkRequestDto.getPingList();

        String key = "ping_list";

        List<PingRequestDto> pl = pingList.getOrDefault(key, new ArrayList<>());

        /* gps 데이터 리스트를 Ping으로 변환해 List 저장 */
        for (PingRequestDto prd : reqPingList){
            pl.add(prd);
        }

        // hashMap의 key: ping_list에 List: pl 값 저장
        pingList.put(key, pl);

        Walk walk = walkService.walkOver(walkRequestDto, key, pingList, petId);
        return new ResponseEntity<Walk>(walk, HttpStatus.OK);
    }

    // 특정 반려동물의 산책 모두 가져오기
    @GetMapping("/api/pets/{petId}/walks/")
    @ResponseBody
    public ResponseEntity<List<Walk>> getAllWalk(@PathVariable Long petId){
        return new ResponseEntity<List<Walk>>(walkService.findAllByPetId(petId),HttpStatus.OK);
    }

    // 특정 반려동물의 산책 하나 가져오기
    @GetMapping("/api/pets/{petId}/walks/{walkId}")
    @ResponseBody
    public ResponseEntity<Optional<Walk>> getWalk(@PathVariable Long walkId, @PathVariable Long petId){
        return new ResponseEntity<Optional<Walk>>(walkService.findById(walkId),HttpStatus.OK);
    }


    // 한 강아지의 산책 전부 삭제
    @DeleteMapping("/api/pets/{petId}/walks")
    @ResponseBody
    public ResponseEntity<String> deleteAllWalk(@PathVariable Long petId){
        if(walkService.deleteAll(petId)){
            return new ResponseEntity<String>("모든 산책을 삭제했습니다",HttpStatus.OK);
        }else{
            return new ResponseEntity<>("삭제에 실패했습니다",HttpStatus.BAD_REQUEST);
        }
    }

    // 한 강아지의 특정 산책 삭제
    @DeleteMapping("/api/pets/{petId}/walk/{walkId}")
    @ResponseBody
    public ResponseEntity<String> deleteWalk(@PathVariable Long walkId, @PathVariable String petId){
        if(walkService.deleteWalk(walkId)){
            return new ResponseEntity<String>("산책을 삭제했습니다.", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("삭제에 실패했습니다.",HttpStatus.BAD_REQUEST);
        }
    }

    // HotPlace 테스트용
    @GetMapping("/api/walks/test")
    @ResponseBody
    public ResponseEntity<List<Walk>> findHotPlaceTest(){
        List<Walk> walkList = walkService.findHotPlace();
//        if(!walkList.isEmpty()){
//            return new ResponseEntity<List<Walk>>(walkList, HttpStatus.OK);
//        }else{
//            return new ResponseEntity<List<Walk>>(walkList,HttpStatus.NO_CONTENT);
//        }
        return new ResponseEntity<List<Walk>>(walkList, HttpStatus.OK);
    }

    // *********** 산책로 ***********

    // 특정 산책의 산책로 가져오기
//    @GetMapping("/api/walks/{walkId}/roads/")
//    @ResponseBody
//    public ResponseEntity<?>getRoadMap(@PathVariable Long walkId) {
//        RoadMap roadMap = walkService.findRoadMapById(walkId);
//        if (roadMap.getRoadMapName() != null) {
//            return new ResponseEntity<RoadMap>(walkService.findRoadMapById(walkId), HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
}
