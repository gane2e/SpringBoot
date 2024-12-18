package com.shop.dto;

import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {

    private Long id; /* item_img_id */
    private String imgName; /* 이미지 파일명 */
    private String oriImgName; /* 원본 이름 */
    private String imgUrl; /* 이미지 조회 경로 */
    private String repimgYn; /* 대표 이미지(썸네일) */


    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg) {

        /* ItemImg 엔터티객체로 받아서, ItemImgDto로 변환 */
        return modelMapper.map(itemImg, ItemImgDto.class);
    }


}
