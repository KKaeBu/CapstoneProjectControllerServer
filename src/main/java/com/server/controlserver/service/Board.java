package com.server.controlserver.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Board {
  private int Size; // 스팟의 갯수
  private double Width; // 보드의 가로 길이
  private double Height; // 보드의 세로 길이
  private double spotSize; // 보드를 스팟별로 나누는 크기
  private HashMap<String, Spot> spotList; // 보드 내 나눠진 구역(스팟)의 리스트(핑이 들어있는 스팟만 기록)
}
