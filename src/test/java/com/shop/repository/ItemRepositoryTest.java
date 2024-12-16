package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest(){
        Item item = new Item();

        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        item.setItemSellStatus(ItemSellStatus.SELL);

        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem);
    }

    @Test
    @DisplayName("레코드 삭제")
    public void deleteItemTest(){
        itemRepository.deleteById(2L);
        System.out.println("레코드삭제완료~");
    }

    @Test
    @DisplayName("레코드 조회")
    public void findItemTest(){
        Optional<Item> item = itemRepository.findById(1L);
        System.out.println("--------------");
        item.ifPresent(System.out::println);
    }

    @Test
    @DisplayName("상품 저장 테스트")
    public void createItemTest2(){

        for (int i=0; i<10; i++){
            Item item = new Item();

            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000*i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setStockNumber(100*i);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            item.setItemSellStatus(ItemSellStatus.SELL);
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("레코드 개수 조회")
    public void countItemTest(){
        long count = itemRepository.count();
        System.out.println("count =====> " + count);
    }

    @Test
    @DisplayName("전체 레코드 조회")
    public void selectAllTest(){
        List<Item> items = itemRepository.findAll();
        items.forEach(System.out::println);
    }

    @Test
    @DisplayName("레코드 수정")
    public void updateItemTest(){
        /*null이 올 수 있는 값을 감싸는 Wrapper 클래스로, 참조하더라도 NPE가 발생하지 않도록 도와줌*/
        Optional<Item> optItem = itemRepository.findById(1L);
        Item item = optItem.get();

        item.setItemNm("수정된 상품 이름");
        item.setPrice(9999);

        itemRepository.save(item);
        /*업데이트가 따로 있진 않고,, 조회했을 때 데이터가 다르면
                save시 자동으로 update문 실행해줌*/
    }

    @Test
    @DisplayName("상품명 조회(테스트 상품5)")
    public void selectByItemNnTest(){
        String name = "테스트 상품5";
        List<Item> itemNm =  itemRepository.findItemByItemNm(name);

        itemNm.forEach(item -> log.info(item.toString()));
    }


    @Test
    @DisplayName("상품명 조회(와일드카드)")
    public void selectByItemNnTest2(){
        String name = "상품";
        List<Item> itemNm =  itemRepository.findByItemNmContaining(name);

        itemNm.forEach(item -> log.info(item.toString()));
    }


    @Test
    @DisplayName("price 가격 이상 조회")
    public void selectByItemNnTest3(){
        List<Item> itemNm =  itemRepository.findItemByPriceGreaterThan(50000);
        itemNm.forEach(item -> log.info(item.toString()));
    }


    @Test
    @DisplayName("order by desc")
    public void selectByItemNmContainingOrderByDescTest(){
        String name = "상품";
        List<Item> itemNm =  itemRepository.findItemByItemNmContainingOrderByPriceDesc(name);
        itemNm.forEach(item -> log.info(item.toString()));
    }
    @Test
    @DisplayName("ItemDetail")
    public void selectByItemDetail(){
        List<Item> itemNm =  itemRepository.findByItemDetail("상품");
        itemNm.forEach(item -> log.info(item.toString()));
    }
 @Test
    @DisplayName("ItemDetail")
    public void selectByItemDetail2(){
        List<Item> itemNm =  itemRepository.findByItemDetail2("상품");
        itemNm.forEach(item -> log.info(item.toString()));
    }



}