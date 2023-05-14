package com.server.controlserver.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="road_tb")
@Entity
public class RoadMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "road_map_name")
    private String roadMapName; //산책로이름
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roadMap")
    @Column(name = "ping_list")
    private List<Ping> pingList = new ArrayList<Ping>(); //핑리스트
}