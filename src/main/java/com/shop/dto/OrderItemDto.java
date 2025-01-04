package com.shop.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
  
  private String itemNm; //상품명
  
  private int count;
  
  private int orderPrice; //주문금액
  private String imgUrl; //상품 이미지 경로
  
  public OrderItemDto(String itemNm, int count, int orderPrice, String imgUrl) {
    this.itemNm = itemNm;
    this.count = count;
    this.orderPrice = orderPrice;
    this.imgUrl = imgUrl;
  }
}
