package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;

@Entity
@Table(name = "item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id /*기본키 설정*/
    @Column(name = "item_id") /*컬럼명 지정*/
    @GeneratedValue(strategy = GenerationType.IDENTITY) /*자동증가*/
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
    private String itemDetail; //상품 상세 설명

    /* ENUM타입 명시 */
    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    /* itemFormDto -> item 으로 변환 */
   public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

/*    private static ModelMapper modelMapper = new ModelMapper();

    public static void updateItem(ItemFormDto itemFormDto) {
        modelMapper.map(itemFormDto, Item.class);
    }*/


}
