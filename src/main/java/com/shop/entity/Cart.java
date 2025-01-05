package com.shop.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity{ //기존 regTime, updateTime 변수 삭제 후 BaseEntity 상속
  
  @Id
  @Column(name = "cart_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @OneToOne(fetch = FetchType.LAZY)  //지연로딩 설정
  @JoinColumn(name = "member_id")
  private Member member;
  
  
  //멤버 한 명당 한 개의 장바구니
  public static Cart createCart(Member member) {
    Cart cart = new Cart();
    cart.setMember(member);
    return cart;
  }
}
