package com.server.controlserver.controller;

import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.Walk;
import com.server.controlserver.dto.PingRquestDto;
import com.server.controlserver.dto.WalkRequestDto;
import com.server.controlserver.service.WalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WalkController {

    private WalkService walkService;
    private final ConcurrentHashMap<String, List<PingRquestDto>> pingList;


    @Autowired
    public WalkController(WalkService walkService) {
        this.walkService = walkService;
        this.pingList = new ConcurrentHashMap<String, List<PingRquestDto>>();
    }

    @PostMapping("/api/pets/{petId}/walk")
    public ResponseEntity<?> walkEnd(@PathVariable Long petId, @RequestBody WalkRequestDto walkRequestDto) {
        System.out.println("petId: " + petId);
        System.out.println("walkRequest: " + walkRequestDto);
        System.out.println("PingList: " + walkRequestDto.getPingList());

        /* gps 데이터 리스트를 Ping으로 변환해 List 저장 */
        for (PingRquestDto pld : walkRequestDto.getPingList()){
            System.out.println("ping" + walkRequestDto.getPingList().indexOf(pld) + ": " + pld);
        }

        String key = "ping_list";

//        List<PingRquestDto> pl = pingList.getOrDefault(key, new ArrayList<>());

//        pl.add(pingRquestDto);

//        pingList.put(key, pl);

//        Long result = transmitterService.save(pingRquestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }



    /* ******** 산책 **********

    // 산책 생성
    @PostMapping("/api/walk")

    // 산책 가져오기
    @GetMapping("/api/walk/{walkId}")
    @GetMapping("/api/pets/{petId}/walk/{walkId}")

    // 한 강아지의 산책목록 전부 가져오기
    @GetMapping("/api/pets/{petId}/walk")

    // 한 강아지의 산책목록 중 하나 가져오기
    @GetMapping("/api/pets/{petId}/walk/{walkId}")

    // 한 강아지의 산책 전부 삭제
    @DeleteMapping("/api/pets/{petId}/walk")

    // 한 강아지의 특정 산책 삭제
    @DeleteMapping("/api/pets/{petId}/walk/{walkId}")

    */
}
