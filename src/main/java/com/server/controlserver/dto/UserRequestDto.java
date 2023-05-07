package com.server.controlserver.dto;


import com.server.controlserver.domain.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    public User toEntity(){
        return User.builder()
                .userId(this.userId)
                .name(this.name)
                .address(this.address)
//                .password(new BCryptPasswordEncoder(10).encode(this.password))
                .password(this.password)
                .phone(this.phone)
                .build();
    }
}
