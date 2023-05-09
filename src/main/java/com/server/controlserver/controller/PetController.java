package com.server.controlserver.controller;

import com.server.controlserver.dto.PetRequestDto;
import com.server.controlserver.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PetController {

    private PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    // 펫 추가
    @PostMapping("/api/pets")
    @ResponseBody
    public Long signup (PetRequestDto petRequestDto) {
        System.out.println("petRequestDto: " + petRequestDto);
        Long result = petService.join(petRequestDto);
        return result;
    }
}
