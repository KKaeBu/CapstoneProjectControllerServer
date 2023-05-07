package com.server.controlserver.service;

import com.server.controlserver.dto.LoginRequestDto;
import com.server.controlserver.dto.UserRequestDto;
import com.server.controlserver.repository.PetRepository;
import com.server.controlserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.server.controlserver.domain.User;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    public Long join(UserRequestDto userRequestDto){
        User user = userRequestDto.toEntity();
        validateDuplicateUser(user);

        userRepository.save(user);
        return user.getId();
    }

    public String Login(String inputUserId, String inputPassword){
        User user = userRepository.findByUserId(inputUserId).get();

        if (user.getUserId() == null){
            System.out.println("잘못된 사용자 입력됨.");
            return null;
        }

        // Bcrypt 암호화
//        String hashedPassword = user.getPassword();
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(bcryptStrength);
//        System.out.println("Bcrypt 비밀번호 대조 결과: "+bCryptPasswordEncoder.matches(inputPassword,hashedPassword));

        // 비밀번호가 맞다면
//        if (bCryptPasswordEncoder.matches(inputPassword,hashedPassword)){
//            String token = jwtProvider.createToken(user.getUserId());
//            String claims = jwtProvider.parseJwtToken("Bearer "+ token); // 토큰 검증
//            return token;

        if(inputPassword.equals(user.getPassword())){
            String token = jwtProvider.createToken(user.getUserId());
            String claims = jwtProvider.parseJwtToken("Bearer "+ token); // 토큰 검증
            return token;
        }else {
            System.out.println("잘못된 비밀번호 입력됨.");
            return null;
        }
    }

    /* 중복 user 조회 */
    private void validateDuplicateUser(User user) {
        userRepository.findByName(user.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
