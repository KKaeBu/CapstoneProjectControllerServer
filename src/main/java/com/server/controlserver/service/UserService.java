package com.server.controlserver.service;

import com.server.controlserver.dto.UserRequestDto;
import com.server.controlserver.repository.PetRepository;
import com.server.controlserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.server.controlserver.domain.User;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Autowired
    public UserService(UserRepository userRepository, PetRepository petRepository) {
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    public Long join(UserRequestDto userRequestDto){
        User user = userRequestDto.toEntity();
        validateDuplicateUser(user);

        userRepository.save(user);
        return user.getId();
    }

    /* 중복 user 조회 */
    private void validateDuplicateUser(User user) {
        userRepository.findByName(user.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }
}
