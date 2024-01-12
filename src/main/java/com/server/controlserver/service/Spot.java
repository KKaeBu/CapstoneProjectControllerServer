package com.server.controlserver.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Spot {
  /*
   * 바둑판의 인덱스 순서는 아래와 같다.
   * 16 17 18 19 20
   * 11 12 13 14 15
   * 6  7  8  9  10
   * 1  2  3  4  5
   * */
  private int number; // 바둑판 내, 스팟의 위치(인덱스 번호)
  private boolean isHot; // 해당 스팟이 핫스팟인지 여부
  private double width; // 한 스팟의 가로 길이
  private double height; // 한 스팟의 세로 길이
  private double latitude; // 스팟의 중심 latitude 좌표
  private double longitude; // 스팟의 중심 longitude 좌표
  private double minLatRange; // 해당 스팟의 latitude 최소 좌표 범위 값
  private double maxLatRange; // 해당 스팟의 latitude 최대 좌표 범위 값
  private double minLngRange; // 해당 스팟의 longitude 최대 좌표 범위 값
  private double maxLngRange; // 해당 스팟의 longitude 최대 좌표 범위 값
  private List<Coord> coordList; // 해당 스팟에 있는 좌표열 리스트
}
