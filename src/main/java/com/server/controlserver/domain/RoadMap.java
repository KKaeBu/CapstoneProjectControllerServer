package com.server.controlserver.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class RoadMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "road_map_name")
    private String roadMapName; //산책로이름
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roadMap")
    @JsonManagedReference
    @Column(name = "ping_list")
    private List<Ping> pingList; //핑리스트
}