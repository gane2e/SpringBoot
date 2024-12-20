package com.shop.service;

import com.shop.dto.OrderDto;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    /* 상품번호 , 수량, 회원ID */
    public Long order(OrderDto orderDto, String email) {

        /*itemId:{1}*/
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow( () -> new EntityNotFoundException());
        
        /* 회원정보 가져오기 (test@test.com) */
        Member member = memberRepository.findByEmail(email);
        /* member:[회원정보] */

        /* 주문저장 리스트 */
        List<OrderItem> orderItemList = new ArrayList<>();

        /* 상품명과 갯수로 주문 신청 */
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        
        /* 여러 상품 주문 */
        orderItemList.add(orderItem);

        /*  */
        Order order = Order.createOrder(member, orderItemList);

        /* DB orders 테이블 저장 */
        orderRepository.save(order);

        /* 저장한 order_id return */
        return order.getId();
    }
}
