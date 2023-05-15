package com.server.controlserver.dto;

import com.server.controlserver.domain.Walk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponseDto {
    private String name; //이름
    private int age; //나이
    private String sex; //성별
    private float weight; //몸무게
    private Boolean isNeutered;//중성화 여부 (true = 1, false = 0)
    private String species; //종
    private List<Walk> walkList; //산책
    private Date createTime; // default값으로 저장시간 자동으로 추가
}
