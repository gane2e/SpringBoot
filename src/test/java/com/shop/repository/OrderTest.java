package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional //테스트후 롤백
class OrderTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EntityManager em;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("지연 로딩 테스트")
    @Commit
    public void lazyLoadingTest(){
        Order order = this.createOrder();

        Long orderItemID = order.getOrderItems().get(0).getId();

        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemID).orElseThrow(EntityNotFoundException::new);
        log.info("-----> " + orderItem.getOrder().getClass());
    }

    public Item createItem(){

        Item item = new Item();

        item.setItemNm("test상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL); //판매중상태
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    @Commit
    public void cascadeTest(){

        Order order = new Order(); /* 주문 객체 생성 */

        for(int i=0; i<3; i++) {

            Item item = this.createItem(); /* 임시 데이터 저장 */
            
            itemRepository.save(item); /* 상품(Item) 임시 데이터 DB 저장 */

            OrderItem orderItem = new OrderItem(); /* 주문내역 객체 생성 */
            
            orderItem.setItem(item); /* 주문내역 임시 데이터 */
            orderItem.setCount(10); /* 주문내역 임시 데이터 */
            orderItem.setOrderPrice(1000); /* 주문내역 임시 데이터 */
            orderItem.setOrder(order); /* 주문 엔티티를 저장하면서 주문 상품 엔티티도 함께 저장    */
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow( () -> new EntityNotFoundException() );
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    public Order createOrder(){
        Order order = new Order();
        for (int i=0; i<3; i++) {

            Item item = this.createItem();
            itemRepository.save(item); /* DB저장 */

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    @Commit
    public void orderRemoveTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }


}