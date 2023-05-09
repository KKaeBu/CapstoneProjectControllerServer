package com.server.controlserver.service;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.User;
import com.server.controlserver.dto.PetRequestDto;
import com.server.controlserver.dto.UserRequestDto;
import com.server.controlserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PetService {
    private final ActivityRepository activityRepository;
    private final GPSRepository gpsRepository;
    private final PetRepository petRepository;
    private final PingRepository pingRepository;
    private final UserRepository userRepository;
    private final WalkRepository walkRepository;

    @Autowired
    public PetService(ActivityRepository activityRepository, GPSRepository gpsRepository, PetRepository petRepository, PingRepository pingRepository, UserRepository userRepository, WalkRepository walkRepository) {
        this.activityRepository = activityRepository;
        this.gpsRepository = gpsRepository;
        this.petRepository = petRepository;
        this.pingRepository = pingRepository;
        this.userRepository = userRepository;
        this.walkRepository = walkRepository;
    }

    // 펫 등록
    public Long join(PetRequestDto petRequestDto){
        Pet pet = petRequestDto.toEntity();
        petRepository.save(pet);
        return pet.getId();
    }
}
