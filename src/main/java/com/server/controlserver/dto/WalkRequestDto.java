package com.server.controlserver.dto;

import com.server.controlserver.domain.Activity;
import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;
import com.server.controlserver.domain.Walk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRequestDto {
    private String roadMapName;
    private String walkedTime;
    private float travelDistance;
    private int burnedCalories;
    private List<Ping> pingList;
    private Date walkDate;

    public RoadMap RoadMapToEntity() {
        return RoadMap.builder()
                .roadMapName(this.roadMapName)
                .pingList(this.pingList)
                .build();
    }

    public Activity ActivityToEntity() {
        return Activity.builder()
                .walkedTime(this.walkedTime)
                .travelDistance(this.travelDistance)
                .burnedCalories(this.burnedCalories)
                .build();
    }

    public Walk WalkToEntity(RoadMap roadMap, Activity activity) {
        return Walk.builder()
                .startPoint(this.pingList.get(0))
                .endPoint(this.pingList.get(pingList.size()-1))
                .roadMap(roadMap)
                .activity(activity)
                .walkDate(this.walkDate)
                .build();
    }
}
