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
    private Long latitude; //위도
    private Long longitude; //경도
    private Long altitude; //고도
    @Column(name = "create_time")
    @CreationTimestamp
    private Date createTime; // default값으로 저장시간 자동으로 추가
}
