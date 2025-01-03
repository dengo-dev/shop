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
public class Order {
  
  @Id
  @GeneratedValue
  @Column(name = "order_id")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;
  
  private LocalDateTime orderDate;
  
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;
  
  @OneToMany(mappedBy = "order",cascade = CascadeType.ALL) //orderItem에 있는 Order에 의해 관리된다는 의미
  private List<OrderItem> orderItems = new ArrayList<>(); //하나의 주문이 여러개의 주문 상품을 갖기 때문에 List사용
  
  
  private LocalDateTime regTime;
  
  private LocalDateTime updateTime;
}
