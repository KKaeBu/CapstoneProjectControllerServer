package com.server.controlserver.controller;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.service.TransmitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TransmitterController {
    private TransmitterService transmitterService;

    @Autowired
    public TransmitterController(TransmitterService transmitterService) {
        this.transmitterService = transmitterService;
    }

    @PostMapping("/api/gps")
    @ResponseBody
    public Long GPS(@RequestBody GPS gps) {
        Long result = transmitterService.save(gps);
        return result;
    }
}
