package com.server.controlserver.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="pet_tb")
@Entity
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name; //이름
    @Column(nullable = false)
    private int age; //나이
    @Column(nullable = false)
    private String sex; //성별
    @Column(nullable = false)
    private float weight; //몸무게
    @Column(name = "is_neutered", nullable = false)
    private Boolean isNeutered;//중성화 여부 (true = 1, false = 0)
    @Column(nullable = false)
    private String species; //종
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
    @JsonManagedReference
    @Column(name = "walk_list")
    private List<Walk> walkList; //산책
    @Column(name = "create_time", nullable = false)
    @CreationTimestamp
    private Date createTime; // default값으로 저장시간 자동으로 추가
}
