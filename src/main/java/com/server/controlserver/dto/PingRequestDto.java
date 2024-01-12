package com.server.controlserver.dto;

import com.server.controlserver.domain.GPS;
import com.server.controlserver.domain.Ping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PingRequestDto {
  private double latitude; // 위도
  private double longitude; // 경도
  private double altitude; // 고도
  private Date createTime; // 핑이 찍힌 시간

  public Ping toPingEntity() {
    return Ping.builder()
        .latitude(this.latitude)
        .longitude(this.longitude)
        .altitude(this.altitude)
        .createTime(this.createTime)
        .build();
  }

  public GPS toGPSEntity() {
    return GPS.builder()
        .latitude(latLngFloor6(this.latitude))
        .longitude(latLngFloor6(this.longitude))
        .altitude(this.altitude)
        .build();
  }

  /** 위도, 경도, 고도 등의 값을 소수점 6자리까지만 나타내도록 잘라주는 함수 */
  public double latLngFloor6(double latLng) {
    double floorCoord = Math.floor(latLng * 1000000) / 1000000.0;

    // 소수점 계산은 정확하게 안나오기 때문에
    // BigDecimal을 사용함
    BigDecimal result = new BigDecimal(String.valueOf(floorCoord));
    BigDecimal correctionValue = new BigDecimal(String.valueOf(0.000001)); // 보정값

    // 소수점 자리수가 6자리보다 적으면 보정값을 더해줌
    if (result.scale() < 6)
      return result.add(correctionValue).doubleValue(); // 이런다고 원본 값이 바뀌진 않음 그래서 바로 리턴해줘야댐

    // BigDecimal에서 double로 값을 꺼낼땐 doubleValue()를 사용
    return result.doubleValue();
  }
}
