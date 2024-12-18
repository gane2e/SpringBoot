package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    /*한 장바구니(pk)의 한 아이템(pk) 과 개수(count) 행 저장*/
    /* M:N 관계 */
    /* [Cart] -> (CartItem) <- [Item] */


    /* 장바구니 행 별 키값 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY ,  cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) /* cart 테이블 조인 */
    @JoinColumn(name = "cart_id")
    private Cart cart;
    
    @ManyToOne(fetch = FetchType.LAZY ,  cascade = {CascadeType.PERSIST, CascadeType.REMOVE}) /* 상품 테이블 조인 */
    @JoinColumn(name = "item_id")
    private Item item;
    
    /* 장바구니에 담은 아이템 개수 */
    private int count;

    
}
