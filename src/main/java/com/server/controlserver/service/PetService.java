package com.server.controlserver.service;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.User;
import com.server.controlserver.dto.PetRequestDto;
import com.server.controlserver.dto.PetResponseDto;
import com.server.controlserver.dto.UserRequestDto;
import com.server.controlserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
  public PetService(
      ActivityRepository activityRepository,
      GPSRepository gpsRepository,
      PetRepository petRepository,
      PingRepository pingRepository,
      UserRepository userRepository,
      WalkRepository walkRepository) {
    this.activityRepository = activityRepository;
    this.gpsRepository = gpsRepository;
    this.petRepository = petRepository;
    this.pingRepository = pingRepository;
    this.userRepository = userRepository;
    this.walkRepository = walkRepository;
  }

  // 펫 등록
  public PetResponseDto join(PetRequestDto petRequestDto) {
    Pet pet = petRequestDto.toEntity();
    petRepository.save(pet);
    return new PetResponseDto(
        pet.getId(),
        pet.getName(),
        pet.getAge(),
        pet.getSex(),
        pet.getWeight(),
        pet.getIsNeutered(),
        pet.getSpecies());
  }

  public PetResponseDto findById(Long id) {
    Pet pet = petRepository.findById(id).get();
    return new PetResponseDto(
        pet.getId(),
        pet.getName(),
        pet.getAge(),
        pet.getSex(),
        pet.getWeight(),
        pet.getIsNeutered(),
        pet.getSpecies());
  }

  public PetResponseDto findRandomPet(Long id) {
    List<Pet> pet = petRepository.findAll();
    Long randomId = id;
    Pet randomPet = new Pet();
    boolean isFailed = true;

    while (isFailed) {
      randomId = (long) (Math.random() * pet.size()); // 랜덤한 펫의 Id
      System.out.println("새로 발급한 randomId " + randomId);
      if (id != randomId) {
        if (!petRepository.findById(randomId).isEmpty()) {
          randomPet = petRepository.findById(randomId).get();
          isFailed = false;
        }
      }
    }
    return new PetResponseDto(
        randomPet.getId(),
        randomPet.getName(),
        randomPet.getAge(),
        randomPet.getSex(),
        randomPet.getWeight(),
        randomPet.getIsNeutered(),
        randomPet.getSpecies());
  }
}
