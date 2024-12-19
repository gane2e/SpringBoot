package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        /* 상품 등록 */
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

/*
        Iterator<MultipartFile> iterator = itemImgFileList.iterator();
        while (iterator.hasNext()) {
            MultipartFile file = iterator.next();
            // imgName이 null이면 해당 객체를 리스트에서 제거
            if (file.getOriginalFilename() == null || file.getOriginalFilename().equals("")) {
                iterator.remove();
            }
        }*/


        /* 이미지 등록 */
        for(int i=0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0){
                itemImg.setRepimgYn("Y"); /* 대표 이미지 */
            } else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    /* 아이템 상세페이지 정보 읽기 START  */
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId) {
        List<ItemImg> itemImgList
                = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        /* 아이디에 해당하는 이미지를 오름차순으로 List 가져옴 */

        /* itemImg -> itemImgDto 로 변환함. */
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList){
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow( () -> new EntityNotFoundException("Item not found") );
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }
    /* 아이템 상세페이지 정보 읽기 END  */


    /* 상품 정보 변경(업데이트) START */
    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        /* 상품 수정 */
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow( () -> new EntityNotFoundException("Item not found") );
        
        /* itemFormDto의 변경 값들을 Item entity에 저장 */
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        /* 이미지 등록 */
        for(int i=0; i<itemImgIds.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
        }
        return item.getId();
    }
    /* 상품 정보 변경(업데이트) END */


    /* 검색처리 START */
    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }
    /* 검색처리 END */


    /* 메인페이지에 보여줄 상품 데이터를 조회 */
    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
    /*  */


}
