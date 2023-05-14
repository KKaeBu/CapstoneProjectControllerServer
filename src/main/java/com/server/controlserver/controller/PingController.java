package com.server.controlserver.controller;

import com.server.controlserver.dto.PingRquestDto;
import com.server.controlserver.service.WalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class PingController {
    private TransmitterController transmitterController;
    private WalkService walkService;
    private ConcurrentHashMap<String, List<PingRquestDto>> pingList;

    @Autowired
    public PingController(TransmitterController transmitterController, WalkService walkService) {
        this.transmitterController = transmitterController;
        this.walkService = walkService;
        this.pingList = new ConcurrentHashMap<>();
    }

    // GPS정보 (위도,경도) ping 저장
    @PostMapping("/api/pings")
    @ResponseBody
    public ResponseEntity<?> Ping(@RequestBody PingRquestDto pingRquestDto) {
        System.out.println("pingRequest: " + pingRquestDto);
        String key = "ping_list";

        List<PingRquestDto> pl = pingList.getOrDefault(key, new ArrayList<>());

        pl.add(pingRquestDto);

        pingList.put(key, pl);

        return ResponseEntity.ok().build();
    }


}
