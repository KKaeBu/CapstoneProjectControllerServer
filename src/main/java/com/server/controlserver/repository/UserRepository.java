package com.server.controlserver.repository;

import com.server.controlserver.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user); // 펫 저장
    User update(User user); // 회원 업데이트
    Optional<User> findById(Long id); //id로 찾아서 반환
    Optional<User> findByName(String name); //이름으로 찾아서 반환
    List<User> findAll(); //모든 회원 반환
}
