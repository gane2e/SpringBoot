package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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

    /*@Transactional(readOnly = true)*/
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


}
