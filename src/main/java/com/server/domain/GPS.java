package com.server.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="gps_tb")
@Entity
public class GPS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long latitude; //위도
    @Column(nullable = false)
    private Long longitude; //경도
    @Column(nullable = false)
    private Long altitude; //고도
    @Column(name = "create_time", nullable = false)
    @CreationTimestamp
    private Date createTime; // default값으로 저장시간 자동으로 추가
}
