package com.server.controlserver.service;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.repository.GPSRepository;
import com.server.controlserver.repository.PingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class TransmitterService {
    private GPSRepository gpsRepository; // 실시간 트래커 위치 정보 저장 db
    private PingRepository pingRepository; // 산책시 트래커 위치 정보 기록 db

    @Autowired
    public TransmitterService(GPSRepository gpsRepository, PingRepository pingRepository) {
        this.gpsRepository = gpsRepository;
        this.pingRepository = pingRepository;
    }

    // 현재 트래커의 위치 저장
    public Long save(GPS gps) {

        gpsRepository.save(gps); // 트래커의 현재 위치 저장
        pingRepository.save(gps);

        return gps.getId();
    }

    // 저장된 트래커의 위치 정보 가져오기
    public Optional<GPS> getLocation(int id) {
        GPS gps = gpsRepository.findLatest(1).get();
        return Optional.ofNullable(gps);
    }
}
