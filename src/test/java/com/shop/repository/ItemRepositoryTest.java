package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
            QItem qItem = QItem.item;

            /* SELECT * FROM item
            * WHERE itemSellStatus = like %SELL% AND
            *       itemDetail like %테스트 상품 상세 설명%
            *  */

            JPAQuery<Item> query = queryFactory.selectFrom(qItem);
            query
                    .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                    .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                    .orderBy(qItem.price.desc());

            List<Item> itemList = query.fetch();
            itemList.forEach(System.out::println);

            long count = query.fetchCount(); /* 권장X */
            log.info("count : " + count);

            int total = itemList.size(); /* 권장O */
            log.info("total : " + total);
        }


    public void createItemList2(){
        for(int i=1;i<=5;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }

        for(int i=6;i<=10;i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());
            itemRepository.save(item);
        }
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트 2")
    public void queryDslTest2(){
        this.createItemList2(); /*아이템 10번만듬*/

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStatus = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price)); /* 10003 이상(gt) */

       /* if(StringUtils.equals(itemSellStatus, ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }*/

        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = /*boolean빌더 조건 AND pageable 조건을 매개변수로 전달*/
                itemRepository.findAll(booleanBuilder,pageable);
                
        log.info("total elements : " + itemPagingResult.getTotalElements());
        itemPagingResult.getContent().forEach(System.out::println);
    }

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