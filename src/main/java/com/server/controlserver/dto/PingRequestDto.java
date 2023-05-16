package com.server.controlserver.dto;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.domain.Ping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PingRequestDto {
    private double latitude; //위도
    private double longitude; //경도
    private double altitude; //고도

    public Ping toPingEntity(){
        return Ping.builder()
                .latitude(this.latitude)
                .longitude(this.longitude)
                .altitude(this.altitude)
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
