package com.server.controlserver.dto;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.User;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PingRquestDto {
    private double latitude; //위도
    private double longitude; //경도
    private double altitude; //고도

    public Ping toEntity(){
        return Ping.builder()
                .latitude(this.latitude)
                .longitude(this.longitude)
                .altitude(this.altitude)
                .build();
    }

    public GPS toEntityGPS() {
        return GPS.builder()
                .latitude(this.latitude)
                .longitude(this.longitude)
                .altitude(this.altitude)
                .build();
    }
}
