package com.server.controlserver.controller;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.domain.RoadMap;
import com.server.controlserver.dto.ActivityRequestDto;
import com.server.controlserver.dto.PingRequestDto;
import com.server.controlserver.service.TransmitterService;
import com.server.controlserver.service.WalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class TransmitterController {
    private final TransmitterService transmitterService;
    private final WalkService walkService;

    @Autowired
    public TransmitterController(TransmitterService transmitterService, WalkService walkService) {
        this.transmitterService = transmitterService;
        this.walkService = walkService;
    }
    //  ******gps******

    // GPS정보 (위도,경도) 클라이언트 -> 서버 전달 (테스트용)
    @PostMapping("/api/gps")
    @ResponseBody
    public ResponseEntity<Long> GPS(@RequestBody PingRequestDto pingRquestDto) {
        Long result = transmitterService.save(pingRquestDto);
        return new ResponseEntity<Long>(result, HttpStatus.OK);
    }

    // GPS정보 (위도,경도) 서버 -> 클라이언트 전달
    @GetMapping("/api/gps")
    @ResponseBody
    public ResponseEntity<GPS> getGPS() {
        GPS gps = transmitterService.getLocation().get();
        return new ResponseEntity<GPS>(gps,HttpStatus.OK);
    }
}
