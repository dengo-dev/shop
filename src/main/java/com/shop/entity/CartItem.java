package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cart_item")
public class CartItem extends BaseEntity{ //기존 regTime, updateTime 변수 삭제 후 BaseEntity 상속
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cart_item_id")
  private Long id;
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id")
  private Cart cart; //하나의 장바구니에 여러개의 아이템을 담을 수 있다.
  
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item; //하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있기에
  
  private int count; //같은 상품을 몇개 담을지
  
  
  public static CartItem createCartItem(Cart cart, Item item, int count) {
    CartItem cartItem = new CartItem();
    cartItem.setCart(cart);
    cartItem.setItem(item);
    cartItem.setCount(count);
    return cartItem;
  }
  
  //해당 상품을 담을 떄 기존 수량에 현재 담을 수량을 더해주는 메소드
  public void addCount(int count) {
    this.count += count;
  }
}
