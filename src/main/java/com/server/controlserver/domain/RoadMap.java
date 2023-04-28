package com.server.controlserver.domain;

import jakarta.persistence.*;
import lombok.*;

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
    @Column()
    private String roadMapName; //산책로이름
    @Column(nullable = false)
    @OneToMany()
    @JoinColumn(name = "ping_list")
    private List<Ping> pingList; //핑리스트
}