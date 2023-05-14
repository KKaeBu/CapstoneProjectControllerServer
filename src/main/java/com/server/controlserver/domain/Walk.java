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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "start_point")
    private Ping startPoint;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "end_point")
    private Ping endPoint;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "road_map_id")
    private RoadMap roadMapId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id")
    private Activity activityId;
    @Column(name = "walk_date")
    private Date walkDate;
}