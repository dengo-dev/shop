package com.shop.service;


import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.dto.OrderItemDto;
import com.shop.entity.*;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
  
  private final ItemRepository itemRepository;
  
  private final MemberRepository memberRepository;
  
  private final OrderRepository orderRepository;
  private final ItemImgRepository itemImgRepository;
  
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
  
  @Transactional
  public Page<OrderHistDto> getOrderList(String email, Pageable pageable) {
    List<Order> orders = orderRepository.findOrders(email, pageable); //주문 목록 조회
    Long totalCount = orderRepository.countOrder(email); //주문 총 개수
    
    List<OrderHistDto> orderHistDtos = new ArrayList<>();
    
    for (Order order : orders) {
      OrderHistDto orderHistDto = new OrderHistDto(order);
      List<OrderItem> orderItems = order.getOrderItems();
      for (OrderItem orderItem : orderItems) {
        ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn
            (orderItem.getItem().getId(), "Y");
        OrderItemDto orderItemDto =
            new OrderItemDto(orderItem, itemImg.getImgUrl());
        orderHistDto.addOrderItemDto(orderItemDto);
      }
      
      orderHistDtos.add(orderHistDto);
    }
    
    return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
  }
  
  @Transactional(readOnly = true)
  public boolean validateOrder(Long orderId, String email) {  //로그인 사용자와 주문 데이터 사용자가 같은지 검사하는 로직
    Member curMember = memberRepository.findByEmail(email);
    
    Order oder = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
    Member savedMember = oder.getMember();
    
    if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
      return false;
    }
    
    return true;
  }
  
  
  //변경감지로인해 트랜잭션이 끝날 때 update쿼리 실행
  public void cancelOrder(Long orderId){
    Order order = orderRepository.findById(orderId)
        .orElseThrow(EntityNotFoundException::new);
    order.cancelOrder();
  }
  
  
  public Long orders(List<OrderDto> orderDtoList, String email) {
    
    Member member = memberRepository.findByEmail(email);
    List<OrderItem> orderItemList = new ArrayList<>();
    
    //주문할 상품 리스트 생성
    for (OrderDto orderDto : orderDtoList) {
      Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
      
      OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
      orderItemList.add(orderItem);
    }
    
    //현재 로그인한 회원, 주문상품 목록을 이용해 주문 엔티티 생성
    Order order = Order.createOrder(member, orderItemList);
    orderRepository.save(order);
    
    return order.getId();
  }
}
