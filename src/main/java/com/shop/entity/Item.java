package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item {

    @Id /*기본키 설정*/
    @Column(name = "item_id") /*컬럼명 지정*/
    @GeneratedValue(strategy = GenerationType.AUTO) /*자동증가*/
    private Long id;

    /* NOt NULL, 50자 */
    @Column(nullable = false, length = 50)
    private String itemNm; //상품명

    /* NOt NULL */
    @Column(nullable = false)
    private int price;

    /* NOt NULL */
    @Column(nullable = false)
    private int stockNumber; //재고수량

    /* NOt NULL */
    @Column(nullable = false)
    private String itemDetail; //상품 상페 설명

    /* ENUM타입 명시 */
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태


    private LocalDateTime regTime; //상품 등록시간
    private LocalDateTime updateTime; //수정시간

}
