package com.server.controlserver.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activity_tb")
@Entity
public class Activity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "walked_time")
  private long walkedTime; // 산책시간

  @Column(name = "burned_calories")
  private int burnedCalories; // 소모칼롤리

  @Column(name = "travel_distance")
  private Double travelDistance; // 산책거리
}
