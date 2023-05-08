package com.server.controlserver.dto;

import com.server.controlserver.domain.User;
import com.server.controlserver.domain.Walk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkRquestDto {
    private Date walkDate; // 산책 날짜

    public Walk toEntity(){
        return Walk.builder()
                .walkDate(this.walkDate)
                .build();
    }
}
