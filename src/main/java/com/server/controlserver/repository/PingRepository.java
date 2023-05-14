package com.server.controlserver.repository;

import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;

import java.util.List;
import java.util.Optional;

public interface PingRepository {
    RoadMap save(List<Ping> ping); // 펫 저장
    Ping update(Ping ping); // 회원 업데이트
    Optional<Ping> findById(Long id); //id로 찾아서 반환
    List<Ping> findAll(); //모든 회원 반환
}
