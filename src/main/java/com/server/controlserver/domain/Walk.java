package com.server.controlserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="walk_tb")
@Entity
public class Walk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "start_point")
    private Ping startPoint;
    @OneToOne
    @JoinColumn(name = "end_point")
    private Ping endPoint;
    @OneToOne
    @JoinColumn(name = "road_map_id")
    private RoadMap roadMapId;
    @OneToOne
    @JoinColumn(name = "activity_id")
    private Activity activityId;
    @Column(name = "walk_date")
    private Date walkDate;
}
