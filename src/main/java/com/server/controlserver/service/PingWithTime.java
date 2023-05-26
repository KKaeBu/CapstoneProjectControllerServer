package com.server.controlserver.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PingWithTime {
    private double latitude; // 위도
    private double longitude; // 경도
    private Long stayTime; // 해당 좌표에 머문 시간
}
