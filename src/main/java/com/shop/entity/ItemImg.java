package com.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "item_img")
@Getter
@Setter
public class ItemImg extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_img_id")
    private Long id;


    private String imgName; /* 이미지 파일명 */

    private String oriImgName; /* 원본 이름 */

    private String imgUrl; /* 이미지 조회 경로 */

    private String repimgYn; /* 대표 이미지(썸네일) */

    /* item_id 상대로 N개의 이미지를 가질 수 있음 */
    /* 다대일 단방향 관계로 매핑 */
    /* 지연로딩 설정하여 매핑된 상품 엔티티 정보가 필요할 경우 데이터를 조회하도록 함 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    
    /* 원본명, 업데이트 이미지명, 이미지 경로를 파라미터로 입력받아 이미지 정보를 업데이트하는 메서드 */
    public void updateItemImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
