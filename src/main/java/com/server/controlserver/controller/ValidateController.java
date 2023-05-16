package com.server.controlserver.controller;

import com.server.controlserver.dto.UserRequestDto;
import com.server.controlserver.dto.UserResponseDto;
import com.server.controlserver.repository.UserRepository;
import com.server.controlserver.service.JwtProvider;
import com.server.controlserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import com.server.controlserver.domain.User;

@Controller
public class ValidateController {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    public ValidateController(JwtProvider jwtProvider, UserService userService, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/api/authorization")
    @ResponseBody
    public ResponseEntity<UserResponseDto> Auth(@RequestHeader(value = "Authorization", required=false) String token){
        System.out.println(token);
        String userId = jwtProvider.parseJwtToken(token);

        if(userId != null){
            UserResponseDto user = userService.findByUserId(userId);
            return new ResponseEntity<UserResponseDto>(user, HttpStatus.OK); /* http state code 200 반환 */
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); /* http state code 400 반환 */
        }
    }
}
