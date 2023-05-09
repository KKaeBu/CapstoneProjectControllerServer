package com.server.controlserver.dto;

import com.server.controlserver.domain.Pet;
import com.server.controlserver.domain.Walk;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetRequestDto {
    private String name; //이름
    private int age; //나이
    private String sex; //성별
    private float weight; //몸무게
    private Boolean isNeutered;//중성화 여부 (true = 1, false = 0)
    private String species; //종

    public Pet toEntity() {
        return Pet.builder()
                .name(this.name)
                .age(this.age)
                .sex(this.sex)
                .weight(this.weight)
                .isNeutered(this.isNeutered)
                .species(this.species)
                .build();
    }
}
