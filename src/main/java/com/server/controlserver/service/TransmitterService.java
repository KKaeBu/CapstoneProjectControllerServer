package com.server.controlserver.service;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.repository.GPSRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class TransmitterService {
    private GPSRepository gpsRepository;

    @Autowired
    public TransmitterService(GPSRepository gpsRepository) {
        this.gpsRepository = gpsRepository;
    }

    public Long save(GPS gps) {

        gpsRepository.save(gps);

        return gps.getId();
    }

    public Optional<GPS> getLocation(int id) {
        GPS gps = gpsRepository.findLastest(1).get();
        return Optional.ofNullable(gps);
    }
}
