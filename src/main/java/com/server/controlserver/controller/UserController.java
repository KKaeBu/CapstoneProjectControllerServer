package com.server.controlserver.controller;

import com.server.controlserver.dto.LoginRequestDto;
import com.server.controlserver.dto.UserRequestDto;
import com.server.controlserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //    홈 화면
    @GetMapping(value = {"", "/api", "/"})
    @ResponseBody
    public String home() {
        System.out.println("접속.");
        return "접속";
    }

    // 사용자 추가
    @PostMapping("/api/users")
    @ResponseBody
    public Long signup(UserRequestDto userRequestDto){
        System.out.println(userRequestDto);
        Long result = userService.join(userRequestDto);
        return result;
    }

    // 사용자 로그인
    @PostMapping("/api/login")
    public ResponseEntity<HttpHeaders> Login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = userService.Login(loginRequestDto.getUserId(), loginRequestDto.getPassword());

        HttpHeaders httpHeaders = new HttpHeaders();

        if (token != null) {
            httpHeaders.add("Authorization", "Bearer " + token);
            return new ResponseEntity<>(httpHeaders, HttpStatus.OK); /* http state code `200` 반환 */
        } else {
            return new ResponseEntity<>(httpHeaders, HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }
/*  *******Service(User)*********

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
