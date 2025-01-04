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
  
  public Long order(OrderDto orderDto, String email) {
    Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
    Member member = memberRepository.findByEmail(email); //이메일을 이용해서 회원 정보 조회
    
    List<OrderItem> orderItemList = new ArrayList<>();
    OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());  // 상품 엔티티 생성
    orderItemList.add(orderItem);
    
    Order order = Order.createOrder(member, orderItemList); //주문 엔티티 생성
    orderRepository.save(order);
    
    return order.getId();
    
  }
}
