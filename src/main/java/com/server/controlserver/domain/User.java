//package com.server.controlserver.domain;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.util.Date;
//
//@Getter
//@Setter
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name="user_tb")
//@Entity
//public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(nullable = false)
//    private String name; //이름
//    @Column(nullable = false)
//    private String address; //주소
//    @Column(nullable = false)
//    private String ID; //아이디
//    @Column(nullable = false)
//    private String password; //비밀번호
//    @Column(nullable = false)
//    private String phone; //핸드폰번호
//    @OneToOne
//    @JoinColumn(name = "my_pet_id")
//    private Pet myPetId;//사용자의 반려동물
//    @Column(name = "create_time", nullable = false)
//    @CreationTimestamp
//    private Date createTime; // default값으로 저장시간 자동으로 추가
//}
