package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem {
  
  @Id
  @GeneratedValue
  @Column(name = "cart_item_id")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "cart_id")
  private Cart cart; //하나의 장바구니에 여러개의 아이템을 담을 수 있다.
  
  @ManyToOne
  @JoinColumn(name = "item_id")
  private Item item; //하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있기에
  
  private int count; //같은 상품을 몇개 담을지
}
