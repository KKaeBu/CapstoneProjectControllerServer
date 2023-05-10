package com.server.controlserver.controller;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.service.TransmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TransmitterController {
    private final TransmitterService transmitterService;

    @Autowired
    public TransmitterController(TransmitterService transmitterService) {
        this.transmitterService = transmitterService;
    }
    //  ******gps******

    // GPS정보 (위도,경도) 클라이언트 -> 서버 전달 (테스트용)
    @PostMapping("/api/gps")
    @ResponseBody
    public Long GPS(@RequestBody GPS gps) {
        Long result = transmitterService.save(gps);
        return result;
    }

    // GPS정보 (위도,경도) 서버 -> 클라이언트 전달
    @GetMapping("/api/gps")
    @ResponseBody
    public GPS getGPS() {
        GPS gps = transmitterService.getLocation(1).get();
        return gps;
    }
}
