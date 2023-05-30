package com.server.controlserver.controller;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.dto.PetRequestDto;
import com.server.controlserver.dto.PetResponseDto;
import com.server.controlserver.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class PetController {

    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    // Pet 추가
    @PostMapping("/api/pets")
    @ResponseBody
    public ResponseEntity<PetResponseDto> signup (@RequestBody PetRequestDto petRequestDto) {
        System.out.println("petRequestDto: " + petRequestDto);
        PetResponseDto newPet = petService.join(petRequestDto);
        return new ResponseEntity<PetResponseDto>(newPet, HttpStatus.CREATED);
    }

    // 본인 Pet 제외 랜덤 Pet 반환
    @GetMapping("/api/otherPets/{Id}")
    @ResponseBody
    public ResponseEntity<PetResponseDto> findOtherPets(@PathVariable("Id")Long Id){
        System.out.println("전달받은 petID : " + Id);
        PetResponseDto otherPet = petService.findRandomPet(Id);
        return new ResponseEntity<PetResponseDto>(otherPet,HttpStatus.OK);
    }

    /*
     * 특정 id 사용자의 반려동물 Get 요청 > 근데 이거 일단 UserController 작성
     * 사용자의 반려동물을 불러오는 것이니 UserController에 있는것이 좋을 것 같아 UserController에 작성
     */

    /* 특정 사용자의 반려동물 중 한 마리 Get 요청
     *   @GetMapping("/api/users/{userId}/pets/{petId}") // need userId & petId
     *   이 API는 여러마리 중 한마리 선택할 때 사용할 API
     *   현재는 각 사용자 간 한 마리의 반려동물을 가지도록 설정 했기때문에, 추후에 작성.
     */

    // * 특정id 사용자의 반려동물 추가 > Get요청과 마찬가지로 UserController에 작성

    // * 특정id 반려동물 전부 삭제 > 이것도 UserController에 작성

    /* 특정id 반려동물 중 한 마리 삭제
     * 현재는 각 사용자 간 한 마리의 반려동물을 가지도록 설정 했기때문에, 추후에 작성.
     */
}
