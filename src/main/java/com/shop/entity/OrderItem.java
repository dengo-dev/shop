package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{ //기존 regTime, updateTime 변수 삭제 후 BaseEntity 상속
  
  @Id
  @GeneratedValue
  @Column(name = "order_item_id")
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)  //지연 로딩 설정
  @JoinColumn(name = "item_id")
  private Item item;   //하나의 상품은 여러 주문에 들어갈 수 있다.
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;  //한 번의 주문에 여러개의 상품을 주문할 수 있다.
  
  private int orderPrice;
  
  private int count;
  

}
