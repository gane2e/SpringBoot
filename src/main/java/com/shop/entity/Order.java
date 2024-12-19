package com.shop.entity;

import com.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL) /* 한 명의 회원은 여러번 주문할 수 있다. */
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; /* 주문일자 */

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; /* 주문상태 */

    /* order는 orderItem을 여러개 가질 수 있다. */
  /*  @OneToMany(mappedBy = "order")*/
  /*  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //모두적용*/
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
                    orphanRemoval = true) //모두적용
    private List<OrderItem> orderItems = new ArrayList<>();

}
