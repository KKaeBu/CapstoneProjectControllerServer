package com.server.controlserver.repository;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;
import com.server.controlserver.domain.Walk;

import java.util.List;
import java.util.Optional;

public interface WalkRepository {
    Walk save(Walk walk, Pet pet); // 펫 저장
    Walk update(Walk walk); // 회원 업데이트
    Walk delete(Walk walk); // 산책 삭제
    Optional<Walk> findById(Long id); //id로 찾아서 반환
    Optional<Walk> findByRoadMapId(Long id); //이름으로 찾아서 반환
    List<Walk> findAll(); //모든 회원 반환
}
