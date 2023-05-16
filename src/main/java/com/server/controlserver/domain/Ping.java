package com.server.controlserver.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="ping_tb")
@Entity
public class Ping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "road_map_id")
    private RoadMap roadMap;
    @Column(nullable = false)
    private double latitude; //위도
    @Column(nullable = false)
    private double longitude; //경도
    @Column(nullable = false)
    private double altitude; //고도
    @Column(name = "time_stamp", nullable = false)
    @CreationTimestamp
    private Date timeStamp; // default값으로 저장시간 자동으로 추가
}