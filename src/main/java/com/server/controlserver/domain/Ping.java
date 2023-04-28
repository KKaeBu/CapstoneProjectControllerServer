package com.server.controlserver.domain;

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