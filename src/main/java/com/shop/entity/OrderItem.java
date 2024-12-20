package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "order_item")
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)  /* order 테이블 조인 */
    @JoinColumn(name = "order_id")
    private Order order; //주문정보

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) /* 상품 테이블 조인 */
    @JoinColumn(name = "item_id")
    private Item item; //주문한 상품

    private int orderPrice; //주문가격

    private int count; //수량


    /* 주문이 생기먄 item에 재고 감소하고 주문내역 추가함 */
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count);
        return orderItem;
    }

    /* 총 주문금액 = 주문수량 * 금액 */
    public int getTotalPrice(){
        return orderPrice * count;
    }

}
