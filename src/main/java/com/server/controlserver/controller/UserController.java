package com.server.controlserver.controller;

import com.server.controlserver.dto.UserRequestDto;
import com.server.controlserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/tests")
    @ResponseBody
    public int test(){
        return 12;
    }

    // 사용자 추가
    @PostMapping("/api/users")
    @ResponseBody
    public Long signup(UserRequestDto userRequestDto){
        System.out.println(userRequestDto);
        Long result = userService.join(userRequestDto);
        return result;
    }

/*  *******Service(User)*********
    // 사용자 로그인
    @PostMapping("/api/login")

    // 특정 id 유저 삭제
    @DeleteMapping("/api/users/{userId}") // need userId(PK)

    // 특정 id를 가진 유저 정보 Get요청
    @GetMapping("/api/users/{userId}") // need userId(PK)

    // 특정 사용자명을 가진 유저 정보 Get요청
    @GetMapping("/api/users/{userName}") //need userName(not null)

    // 모든 유저의 정보 Get 요청
    @GetMapping("/api/users")

    */
}
