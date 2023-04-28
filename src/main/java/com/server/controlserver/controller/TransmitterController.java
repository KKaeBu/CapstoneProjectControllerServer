package com.server.controlserver.controller;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.service.TransmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TransmitterController {
    private TransmitterService transmitterService;

    @Autowired
    public TransmitterController(TransmitterService transmitterService) {
        this.transmitterService = transmitterService;
    }
    //  ******gps******

    // GPS정보 (위도,경도) 클라이언트 -> 서버 전달
    @PostMapping("/api/gps")
    @ResponseBody
    public Long GPS(@RequestBody GPS gps) {
        Long result = transmitterService.save(gps);
        return result;
    }

    //  *******Service(User)*********

    // 사용자 추가
    @PostMapping("/api/singUp")

    // 사용자 로그인
    @PostMapping("/api/login")

    // 특정 id 유저 삭제
    @DeleteMapping("/api/user/{userId}")

    // 특정 id를 가진 유저 정보 Get요청
    @GetMapping("/api/user/{userId}")

    // 특정 사용자명을 가진 유저 정보 Get요청
    @GetMapping("/api/user/{userName}")

    // 모든 유저의 정보 Get 요청
    @GetMapping("/api/users")

    //  ********* 반려동물 정보 ********

    // 특정id 사용자의 반려동물 Get 요청
    @GetMapping("/api/{userId}/pet")

    // 특정id 사용자의 반려동물 추가
    @PostMapping("/api/{userId}/pet")

    // 특정id 반려동물 삭제
    @DeleteMapping("/api/{userId}/pet")

    // ******** 산책 **********

    // 산책 생성
    @PostMapping("/api/walk")

    // 산책 가져오기
    @GetMapping("/api/walk/{walkId}")

    // 특정 사용자의 산책목록 가져오기
    @GetMapping("/api/walk/{userId}")

    // 특정 산책 삭제
    @DeleteMapping("/api/walk/{walkId}")

    // ********** 활동량 ************

    // 특정 산책의 활동량 가져오기
    @GetMapping("/api/activity/{walkId}")

    // *********** 산책로 ***********

    // 특정 산책의 산책로 가져오기
    @GetMapping("/api/walkRoad/{walkId}")

    // 산책 삭제가 있으니까 산책로 만 삭제하는건 없어도되는거 같기도하고
}
