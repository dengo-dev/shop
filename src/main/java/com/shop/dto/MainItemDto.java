package com.shop.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {
  
  private Long id;
  
  private String itemNm;
  
  private String itemDetail;
  
  private String imgUrl;
  
  private Integer price;
  
  //상품 조회 시 DTO객체로 결과를 받는다. -> Item객체로 받고 DTO로 전환없이 바로 DTO를 뽑을 수 있음.
  @QueryProjection
  
  public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
    this.id = id;
    this.itemNm = itemNm;
    this.itemDetail = itemDetail;
    this.imgUrl = imgUrl;
    this.price = price;
  }
}
