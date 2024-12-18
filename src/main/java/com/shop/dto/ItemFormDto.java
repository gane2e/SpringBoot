package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "상세 설명은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고 수량은 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    /* 상품 이미지 정보 저장 */
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();

    /* 이미지 상품 index */
    private List<Long> itemImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    /* ItemFormDto --> item 변환 */
    public Item createItem() {
        return modelMapper.map(this, Item.class);
    }

    /* item -> ItemFormDto 변환 */
    public static ItemFormDto of(Item item) {
        return modelMapper.map(item, ItemFormDto.class);
    }


}
