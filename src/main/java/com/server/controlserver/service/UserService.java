package com.server.controlserver.service;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.dto.LoginRequestDto;
import com.server.controlserver.dto.PetResponseDto;
import com.server.controlserver.dto.UserRequestDto;
import com.server.controlserver.dto.UserResponseDto;
import com.server.controlserver.repository.PetRepository;
import com.server.controlserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.server.controlserver.domain.User;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;
import com.server.controlserver.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final JwtProvider jwtProvider;
    private final static int bcryptStrength = 10;

    @Autowired
    public UserService(UserRepository userRepository, PetRepository petRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.jwtProvider = jwtProvider;
    }

    // 유저 회원가입
    public UserResponseDto join(UserRequestDto userRequestDto){
        Pet pet = userRequestDto.toPetEntity();
        User user = userRequestDto.toUserEntity(pet);

        validateDuplicateUser(user);

        userRepository.save(user);
        petRepository.save(pet);

        PetResponseDto petResponseDto = userRequestDto.toPetResponseEntity(pet.getId()); //pet과의 순서 중요

        return new UserResponseDto(
                user.getName(),
                user.getAddress(),
                user.getUserId(),
                user.getPhone(),
                petResponseDto,
                user.getCreateTime()
        );
    }

    // 유저 로그인 (성공시 토큰 반환)
    public String Login(String inputUserId, String inputPassword){
        User user = userRepository.findByUserId(inputUserId).get();

        if (user.getUserId() == null){
            System.out.println("잘못된 사용자 입력됨.");
            return null;
        }

        //Bcrypt 암호화
        if(BCrypt.checkpw(inputPassword, user.getPassword())){
            String token = jwtProvider.createToken(user.getUserId());
            String claims = jwtProvider.parseJwtToken("Bearer "+ token); // 토큰 검증
            return token;
        }else {
            System.out.println("잘못된 비밀번호 입력됨.");
            return null;
        }
    }

    public UserResponseDto findByUserId(String userId){
        User user = userRepository.findByUserId(userId).get();
        PetResponseDto petResponseDto = new PetResponseDto(
                user.getMyPet().getId(),
                user.getMyPet().getName(),
                user.getMyPet().getAge(),
                user.getMyPet().getSex(),
                user.getMyPet().getWeight(),
                user.getMyPet().getIsNeutered(),
                user.getMyPet().getSpecies()
        );

        return new UserResponseDto(
                user.getName(),
                user.getAddress(),
                user.getUserId(),
                user.getPhone(),
                petResponseDto,
                user.getCreateTime()
        );
    }

    public UserResponseDto findById(Long Id){
        User user = userRepository.findById(Id).get();
        PetResponseDto petResponseDto = new PetResponseDto(
                user.getMyPet().getId(),
                user.getMyPet().getName(),
                user.getMyPet().getAge(),
                user.getMyPet().getSex(),
                user.getMyPet().getWeight(),
                user.getMyPet().getIsNeutered(),
                user.getMyPet().getSpecies()
        );

        return new UserResponseDto(
                user.getName(),
                user.getAddress(),
                user.getUserId(),
                user.getPhone(),
                petResponseDto,
                user.getCreateTime()
        );
    }

    public UserResponseDto findByName(String Name){
        User user = userRepository.findByName(Name).get();
        PetResponseDto petResponseDto = new PetResponseDto(
                user.getMyPet().getId(),
                user.getMyPet().getName(),
                user.getMyPet().getAge(),
                user.getMyPet().getSex(),
                user.getMyPet().getWeight(),
                user.getMyPet().getIsNeutered(),
                user.getMyPet().getSpecies()
        );

        return new UserResponseDto(
                user.getName(),
                user.getAddress(),
                user.getUserId(),
                user.getPhone(),
                petResponseDto,
                user.getCreateTime()
        );
    }

    public List<UserResponseDto> findAllUser(){
        List<User> userList = userRepository.findAll();
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();

        for(User u : userList) {
            PetResponseDto petResponseDto = new PetResponseDto(
                    u.getMyPet().getId(),
                    u.getMyPet().getName(),
                    u.getMyPet().getAge(),
                    u.getMyPet().getSex(),
                    u.getMyPet().getWeight(),
                    u.getMyPet().getIsNeutered(),
                    u.getMyPet().getSpecies()
            );

            userResponseDtoList
                    .add(new UserResponseDto(
                            u.getName(),
                            u.getAddress(),
                            u.getUserId(),
                            u.getPhone(),
                            petResponseDto,
                            u.getCreateTime()
                        )
                    );
        }
        return userResponseDtoList;
    }

    public User deleteUser(String userId){
        User user = userRepository.findByUserId(userId).get();
        return userRepository.delete(user);
    }

    public PetResponseDto findPetByUserId(String userId){
        Pet pet = userRepository.findByUserId(userId).get().getMyPet();
        return new PetResponseDto(
                pet.getId(),
                pet.getName(),
                pet.getAge(),
                pet.getSex(),
                pet.getWeight(),
                pet.getIsNeutered(),
                pet.getSpecies()
        );
    }

    public String deleteMyPet(String userId){
        User user = userRepository.findByUserId(userId).get();

        if(user.getUserId() != null){
            user.setMyPet(null);
            userRepository.update(user);
            return "등록된 반려동물 삭제에 성공했습니다.";
        }else{
            return "반려동물 삭제 실패.";
        }
    }

    /* 중복 user 조회 */
    private void validateDuplicateUser(User user) {
        userRepository.findByUserId(user.getUserId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
