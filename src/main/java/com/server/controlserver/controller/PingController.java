package com.server.controlserver.controller;

import com.server.controlserver.dto.PingRequestDto;
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

  @Autowired
  public PingController(TransmitterController transmitterController, WalkService walkService) {
    this.transmitterController = transmitterController;
    this.walkService = walkService;
  }

  // GPS정보 (위도,경도) ping 저장
  @PostMapping("/api/pings")
  @ResponseBody
  public ResponseEntity<?> Ping(@RequestBody PingRequestDto pingRquestDto) {

    return ResponseEntity.ok().build();
  }
}
