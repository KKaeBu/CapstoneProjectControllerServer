package com.server.controlserver.controller;

import com.server.controlserver.service.WalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WalkController {

    private WalkService walkService;

    @Autowired
    public WalkController(WalkService walkService) {
        this.walkService = walkService;
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
