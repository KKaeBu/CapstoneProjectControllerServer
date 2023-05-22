package com.server.controlserver.dto;


import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.mindrot.jbcrypt.BCrypt;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String userName; //이름
    private String userAddress; //주소
    private String userId; //아이디
    private String userPassword; //비밀번호
    private String userPhone; //핸드폰번호

    private String petName; //이름
    private int petAge; //나이
    private String petSex; //성별
    private float petWeight; //몸무게
    private Boolean petIsNeutered;//중성화 여부 (true = 1, false = 0)
    private String petSpecies; //종

    public User toUserEntity(Pet pet){
        String salt = BCrypt.gensalt();
        return User.builder()
                .userId(this.userId)
                .name(this.userName)
                .address(this.userAddress)
                .password(BCrypt.hashpw(this.userPassword, BCrypt.gensalt()))
//                .password(this.password)
                .phone(this.userPhone)
                .myPet(pet)
                .build();
    }
    public Pet toPetEntity(){
        return Pet.builder()
                .name(this.petName)
                .age(this.petAge)
                .sex(this.petSex)
                .weight(this.petWeight)
                .isNeutered(this.petIsNeutered)
                .species(this.petSpecies)
                .build();
    }

    public PetResponseDto toPetResponseEntity(Long petId) {
        return PetResponseDto.builder()
                .id(petId)
                .name(this.petName)
                .age(this.petAge)
                .sex(this.petSex)
                .weight(this.petWeight)
                .isNeutered(this.petIsNeutered)
                .species(this.petSpecies)
                .build();
    }
}
