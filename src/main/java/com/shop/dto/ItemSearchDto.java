package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemSearchDto {

    private String searchDateType; /* 1일 / 1주일 / 1개월 / 6개월 */
    private ItemSellStatus searchSellStatus; /* 판매 / 품절 */
    private String searchBy; /* 상품명 */
    private String searchQuery=""; /* 검색어(키워드) */
}
