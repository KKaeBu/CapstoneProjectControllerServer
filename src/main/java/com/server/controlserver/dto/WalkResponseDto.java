package com.server.controlserver.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.server.controlserver.domain.Activity;
import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkResponseDto {
    private Ping startPoint;
    private Ping endPoint;
    private RoadMap roadMap;
    private Activity activity;
    private Date walkDate;
}
