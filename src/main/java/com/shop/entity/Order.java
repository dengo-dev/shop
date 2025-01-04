package com.shop.entity;


import com.shop.constant.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity{ //기존 regTime, updateTime 변수 삭제 후 BaseEntity 상속
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;
  
  private LocalDateTime orderDate;
  
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;
  
  @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, //mappedBy는 orderItem에 있는 Order에 의해 관리된다는 의미
  orphanRemoval = true,  //고아 객체를 사용하기 위해 사용
  fetch = FetchType.LAZY)
  private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러개의 주문 상품을 갖기 때문에 List사용
  
  
  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this); //Order엔티티, OrderItem엔티티가 양방향 참조이르모 OrderItem객체에도 Order객체를 세팅
  }
  
  public static Order createOrder(Member member, List<OrderItem> orderItemList) {
    Order order = new Order();
    order.setMember(member);
    for (OrderItem orderItem : orderItemList) {
      order.addOrderItem(orderItem);
      
    }
    order.setOrderStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());
    
    return order;
  }
  
  public int getTotalPrice() {
    int totalPrice = 0;
    for (OrderItem orderItem : orderItems) {
      totalPrice += orderItem.getTotalPrice();
    }
    return totalPrice;
  }
}
