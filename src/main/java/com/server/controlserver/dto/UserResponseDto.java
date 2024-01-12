package com.server.controlserver.dto;

import com.server.controlserver.domain.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
  private String name; // 이름
  private String address; // 주소
  private String userId; // 아이디
  private String phone; // 핸드폰번호
  private PetResponseDto myPet; // 사용자의 반려동물
  private Date createTime; // default값으로 저장시간 자동으로 추가
}
