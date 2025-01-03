package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem {
  
  @Id
  @GeneratedValue
  @Column(name = "order_item_id")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item;   //하나의 상품은 여러 주문에 들어갈 수 있다.
  
  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;  //한 번의 주문에 여러개의 상품을 주문할 수 있다.
  
  private int orderPrice;
  
  private int count;
  
  private LocalDateTime regTime;
  
  private LocalDateTime updateTime;
}
