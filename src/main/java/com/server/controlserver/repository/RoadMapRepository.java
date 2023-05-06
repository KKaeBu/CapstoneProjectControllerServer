package com.server.controlserver.repository;

import com.server.controlserver.domain.RoadMap;

import java.util.List;
import java.util.Optional;

public interface RoadMapRepository {
    RoadMap save(RoadMap roadMap); // 펫 저장
    RoadMap update(RoadMap roadMap); // 회원 업데이트
    Optional<RoadMap> findById(Long id); //id로 찾아서 반환
    Optional<RoadMap> findByRoadMapName(String name); //이름으로 찾아서 반환
    List<RoadMap> findAll(); //모든 회원 반환
}
