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
    private String name; //이름
    private String address; //주소
    private String userId; //아이디
    private String password; //비밀번호
    private String phone; //핸드폰번호

    private String petName; //이름
    private int age; //나이
    private String sex; //성별
    private float weight; //몸무게
    private Boolean isNeutered;//중성화 여부 (true = 1, false = 0)
    private String species; //종

    public User toUserEntity(Pet pet){
        String salt = BCrypt.gensalt();
        return User.builder()
                .userId(this.userId)
                .name(this.name)
                .address(this.address)
                .password(BCrypt.hashpw(this.password, BCrypt.gensalt()))
//                .password(this.password)
                .phone(this.phone)
                .myPet(pet)
                .build();
    }
    public Pet toPetEntity(){
        return Pet.builder()
                .name(this.petName)
                .age(this.age)
                .sex(this.sex)
                .weight(this.weight)
                .isNeutered(this.isNeutered)
                .species(this.species)
                .build();
    }
}
