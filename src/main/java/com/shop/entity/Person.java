package com.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder /*빌더패턴 객체생성*/
@AllArgsConstructor /*모든 필드 값을 파라미터로 받는 생성자를 생성*/
@NoArgsConstructor /*파라미터가 없는 디폴트 생성자를 생성*/
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)   /*기본키를 UUID로 자동생성*/
    private String userId;

    @Column(nullable = false, length = 30)
    private String userName;

    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String address;

    @CreatedDate
    private LocalDateTime regDate;

}
