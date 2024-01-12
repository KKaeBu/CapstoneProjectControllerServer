package com.server.controlserver.service;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.domain.Ping;
import com.server.controlserver.dto.PingRequestDto;
import com.server.controlserver.repository.GPSRepository;
import com.server.controlserver.repository.PingRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TransmitterService {
  private GPSRepository gpsRepository; // 실시간 트래커 위치 정보 저장 db
  private PingRepository pingRepository; // 산책시 트래커 위치 정보 기록 db

  private static ArrayList<Ping> pingList = null;

  @Autowired
  public TransmitterService(GPSRepository gpsRepository, PingRepository pingRepository) {
    this.gpsRepository = gpsRepository;
    this.pingRepository = pingRepository;
  }

  // 현재 트래커의 위치 저장
  public Long save(PingRequestDto pingRquestDto) {
    GPS gps = pingRquestDto.toGPSEntity();
    gpsRepository.save(gps); // 트래커의 현재 위치 저장
    //        pingRepository.save(gps);

    return gps.getId();
  }

  // 저장된 트래커의 위치 정보 가져오기
  public Optional<GPS> getLocation() {
    GPS gps = gpsRepository.findLatest().get();
    return Optional.ofNullable(gps);
  }

  public List<GPS> getGpsList() {
    List<GPS> gpsList = gpsRepository.findGpsList();
    return gpsList;
  }
}
