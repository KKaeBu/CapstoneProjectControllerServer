package com.server.controlserver.dto;

import com.server.controlserver.domain.Activity;
import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;
import com.server.controlserver.domain.Walk;
import com.server.controlserver.repository.PingRepository;
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
    private long walkedTime;
    private float travelDistance;
    private int burnedCalories;
    private List<PingRquestDto> pingList;
    private String walkDate;

    public RoadMap toRoadMapEntity(List<Ping> pingList) {
        return RoadMap.builder()
                .roadMapName(this.roadMapName)
                .pingList(pingList)
                .build();
    }

    public Activity toActivityEntity() {
        return Activity.builder()
                .walkedTime(this.walkedTime)
                .travelDistance(this.travelDistance)
                .burnedCalories(this.burnedCalories)
                .build();
    }

    public Walk toWalkEntity(List<Ping> pingList, RoadMap roadMap, Activity activity) {
        return Walk.builder()
                .startPoint(pingList.get(0))
                .endPoint(pingList.get(pingList.size()-1))
                .roadMap(roadMap)
                .activity(activity)
                .walkDate(this.walkDate)
                .build();
    }
}
