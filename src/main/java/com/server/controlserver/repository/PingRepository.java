package com.server.controlserver.repository;

import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;

import java.util.List;
import java.util.Optional;

public interface PingRepository {
    Ping savePing(Ping ping); // 핑 저장
    RoadMap saveRoadMapPingList(RoadMap roadMap, List<Ping> pingList); // 핑 리스트 저장
    Ping update(Ping ping); // 핑 업데이트
    Optional<Ping> findById(Long id); //id로 찾아서 반환
    List<Ping> findAll(); //모든 핑 반환
}
