package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    /* 상품 */
    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    /* 구매자 */
    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test10@test10.com");
        member.setPassword("123456");
        return memberRepository.save(member);
    }
    
    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item = saveItem(); /*상품생성*/
        Member member = saveMember(); /*구매자생성*/

        OrderDto orderDto = new OrderDto(); /* itemId : null / count : null */
        orderDto.setItemId(item.getId()); /* itemId : 1 */
        orderDto.setCount(10); /* count : 10 */

        /*orderDto[itemId:{1}, count:10]*, getEmail(test@test.com) */
        Long orderId = orderService.order(orderDto, member.getEmail());


        Order order = orderRepository.findById(orderId).get();

        List<OrderItem> orderItems = order.getOrderItems();

        orderItems.forEach(orderItem -> log.info("orderItem ====> " + orderItem));

        int totalPrice = orderDto.getCount() * item.getPrice();

        assertEquals(totalPrice, order.getTotalPrice());
    }



}