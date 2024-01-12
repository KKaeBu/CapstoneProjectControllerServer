package com.server.controlserver.service;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.dto.PetResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coord {
  private double latitude; // 위도
  private double longitude; // 경도
  private Long stayTime; // 해당 좌표에 머문 시간
  private PetResponseDto pet; // 해당 좌표를 표시한 펫의 정보
}
