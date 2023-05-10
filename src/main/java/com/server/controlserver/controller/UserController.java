package com.server.controlserver.controller;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.User;
import com.server.controlserver.dto.LoginRequestDto;
import com.server.controlserver.dto.PetRequestDto;
import com.server.controlserver.dto.UserRequestDto;
import com.server.controlserver.service.PetService;
import com.server.controlserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final PetService petService;

    @Autowired
    public UserController(UserService userService, PetService petService) {
        this.userService = userService;
        this.petService = petService;
    }

    /****************************** <사용자> *****************************************/

    // User 추가
    @PostMapping("/api/users")
    @ResponseBody
    public Long signup(UserRequestDto userRequestDto){
        System.out.println(userRequestDto);
        Long result = userService.join(userRequestDto);
        return result;
    }

    // User 로그인
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

    // 특정 userId 가진 유저 정보 Get요청
    @GetMapping("/api/users/userId/{userId}") //need userId(not null)
    @ResponseBody
    public User findUserByUserId(@PathVariable("userId")String userId){
        return userService.findByUserId(userId);
    }

    // 특정 Id를 가진 유저 정보 Get요청
    @GetMapping("/api/users/Id/{Id}") // need userId(PK)
    @ResponseBody
    public User findUserById(@PathVariable("Id")Long Id){
        return userService.findById(Id);
    }

    // 특정 Name을 가진 유저 정보 Get요청
    @GetMapping("/api/users/Name/{Name}") //need userName(not null)
    @ResponseBody
    public User findUserByName(@PathVariable("Name")String Name){
        return userService.findByName(Name);
    }

    // 모든 User 정보 Get 요청
    @GetMapping("/api/users")
    @ResponseBody
    public List<User> findAllUser(){
        return userService.findAllUser();
    }

    // 특정 userId 유저 삭제
    @DeleteMapping("/api/users/{userId}") // need userId(PK)
    public void removeUser(@PathVariable("userId")String userId){
        User deleteduser = userService.deleteUser(userId);
        System.out.println(deleteduser.getUserId()+"유저 삭제됨.");
    }

    /****************************** <펫> *****************************************/

    // 사용자가 기르는 반려동물 등록 (한 사용자당 반려동물 한 마리)
    @PostMapping("/api/users/{userId}/pets")
    @ResponseBody
    public Long signup (PetRequestDto petRequestDto, @PathVariable String userId) {
        System.out.println("petRequestDto: " + petRequestDto);
        Long result = petService.join(petRequestDto);
        return result;
    }

    // 사용자 반려동물 찾기
    @GetMapping("/api/users/{userId}/pets")
    @ResponseBody
    public Pet getPetInfo(@PathVariable("userId") String userId){
        Pet pet = userService.findPetByUserId(userId);
        return pet;
    }

    // 사용자의 반려동물 삭제
    @DeleteMapping("/api/users/{userId}/pets")
    public void removePet(@PathVariable("userId")String userId){
        userService.deleteMyPet(userId);
    }
}
