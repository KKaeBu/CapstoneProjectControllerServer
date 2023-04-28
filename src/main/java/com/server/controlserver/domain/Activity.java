package com.server.controlserver.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="activity_tb")
@Entity
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column()
    private String walkedTime; //산책시간
    @Column()
    private int burnedCalories;//소모칼롤리
    @Column()
    private float travelDistance;//산책거리
}
