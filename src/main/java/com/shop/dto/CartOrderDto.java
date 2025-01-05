package com.shop.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {
  
  private Long cartItemId;
  
  //장바구니에서 여러개의 상품을 주문하므로 CartOrderDto클래스가 자기 자신을 List로 갖을 수 있게
  private List<CartOrderDto> cartOrderDtoList;
}
