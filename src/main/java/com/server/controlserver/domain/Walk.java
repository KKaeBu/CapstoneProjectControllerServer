package com.server.controlserver.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    @JoinColumn(name = "pet_id")
    private Pet pet;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "start_point")
    private Ping startPoint;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "end_point")
    private Ping endPoint;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "road_map")
    private RoadMap roadMap;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity")
    private Activity activity;
    @Column(name = "walk_date")
    private String walkDate;
}