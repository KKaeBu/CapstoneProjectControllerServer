package com.server.controlserver.controller;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.domain.RoadMap;
import com.server.controlserver.dto.ActivityRequestDto;
import com.server.controlserver.dto.PingRquestDto;
import com.server.controlserver.service.TransmitterService;
import com.server.controlserver.service.WalkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class TransmitterController {
    private final TransmitterService transmitterService;
    private WalkService walkService;
    private ConcurrentHashMap<String, List<PingRquestDto>> pingList;

    @Autowired
    public TransmitterController(TransmitterService transmitterService, WalkService walkService) {
        this.transmitterService = transmitterService;
        this.walkService = walkService;
        this.pingList = new ConcurrentHashMap<>();

    }
    //  ******gps******

    // GPS정보 (위도,경도) 클라이언트 -> 서버 전달 (테스트용)
    @PostMapping("/api/gps")
    @ResponseBody
    public Long GPS(@RequestBody PingRquestDto pingRquestDto) {
        System.out.println("pingRequest: " + pingRquestDto);
        String key = "ping_list";

        List<PingRquestDto> pl = pingList.getOrDefault(key, new ArrayList<>());

        pl.add(pingRquestDto);

        pingList.put(key, pl);

        Long result = transmitterService.save(pingRquestDto);
        return result;
    }

    // GPS정보 (위도,경도) 서버 -> 클라이언트 전달
    @GetMapping("/api/gps")
    @ResponseBody
    public GPS getGPS() {
        GPS gps = transmitterService.getLocation(1).get();
        return gps;
    }

    @PostMapping("/api/end")
    @ResponseBody
    public RoadMap endOfWalk(@RequestBody ActivityRequestDto activityRequestDto) {
        System.out.println("activity: " + activityRequestDto);
        RoadMap roadMap = walkService.endOfWalk(activityRequestDto, pingList);
        System.out.println("roadMap: " + roadMap);
        return roadMap;
    }
}
