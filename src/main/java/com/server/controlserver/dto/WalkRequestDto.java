package com.server.controlserver.dto;

import com.server.controlserver.domain.Activity;
import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;
import com.server.controlserver.domain.Walk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRequestDto {
    private String roadMapName;
    private long walkedTime;
    private float travelDistance;
    private int burnedCalories;
    private List<PingRequestDto> pingList;
    private String walkDate;

    public RoadMap toRoadMapEntity() {
        return RoadMap.builder()
                .roadMapName(this.roadMapName)
                .pingList(new ArrayList<>())
                .build();
    }

    public Activity toActivityEntity() {
        return Activity.builder()
                .walkedTime(this.walkedTime)
                .travelDistance(this.travelDistance)
                .burnedCalories(this.burnedCalories)
                .build();
    }

    public Walk toWalkEntity(Ping startPoint, Ping endPoint, RoadMap roadMap, Activity activity) {
        return Walk.builder()
                .startPoint(startPoint)
                .endPoint(endPoint)
                .roadMap(roadMap)
                .activity(activity)
                .walkDate(this.walkDate)
                .build();
    }
}
