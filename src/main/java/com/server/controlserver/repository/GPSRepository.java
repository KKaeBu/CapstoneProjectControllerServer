package com.server.controlserver.repository;
import com.server.controlserver.domain.GPS;

import java.util.List;
import java.util.Optional;

public interface GPSRepository {
    GPS save(GPS gps); //gps 데이터 저장
    GPS update(GPS gps); //gps 데이터 업데이트
    Optional<GPS> findLatest(); //가장 최근 gps 데이터 전송
    List<GPS> findGpsList(); //테스트용 gps 위치 30개 전송
}
