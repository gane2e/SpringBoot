package com.shop.repository;

import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItemIdOrderByIdDesc(Long itemId);
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    Long item(Item item);
}
