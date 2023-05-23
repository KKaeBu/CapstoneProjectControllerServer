package com.server.controlserver.dto;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.domain.Ping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PingRequestDto {
    private double latitude; //위도
    private double longitude; //경도
    private double altitude; //고도
    private Date createTime; //핑이 찍힌 시간

    public Ping toPingEntity(){
        return Ping.builder()
                .latitude(this.latitude)
                .longitude(this.longitude)
                .altitude(this.altitude)
                .createTime(this.createTime)
                .build();
    }

    public GPS toGPSEntity() {
        return GPS.builder()
                .latitude(this.latitude)
                .longitude(this.longitude)
                .altitude(this.altitude)
                .build();
    }
}
