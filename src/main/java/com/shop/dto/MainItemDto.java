package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

@Getter
@Setter
public class MainItemDto {

    private Long id;
    private String itemNm;
    private String itemDetail;
    private String imgUrl;
    private Integer price;

    /* QueryDsl로 결과 조회 시 MainItemDto 객체로 바로 받아오도록 활용 */
    /* QDomain을 직접 생성하는거임 */
    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail,
                       String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }

}
