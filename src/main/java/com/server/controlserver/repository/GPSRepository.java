package com.server.controlserver.repository;
import com.server.controlserver.domain.GPS;
import java.util.Optional;

public interface GPSRepository {
    GPS save(GPS gps); //gps 데이터 저장
    GPS update(GPS gps); //gps 데이터 업데이트
    Optional<GPS> findLastest();
}
