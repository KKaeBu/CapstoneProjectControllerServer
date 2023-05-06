package com.server.controlserver.repository;

import com.server.controlserver.domain.Walk;

import java.util.List;
import java.util.Optional;

public interface WalkRepository {
    Walk save(Walk walk); // 펫 저장
    Walk update(Walk walk); // 회원 업데이트
    Optional<Walk> findById(Long id); //id로 찾아서 반환
    Optional<Walk> findByRoadMapId(Long id); //이름으로 찾아서 반환
    List<Walk> findAll(); //모든 회원 반환
}
