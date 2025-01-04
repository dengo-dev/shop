package com.shop.entity;


import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "item")
public class Item extends BaseEntity{ //기존 regTime, updateTime 변수 삭제 후 BaseEntity 상속
  
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "item_id")
  private Long id;
  
  
  @Column(nullable = false, length =50)
  private String itemNm; //상품명
  
  
  @Column(name = "price", nullable = false)
  private int price;
  
  @Column(nullable = false)
  private int stockNumber;
  
  @Lob
  @Column(nullable = false)
  private String itemDetail;
  
  @Enumerated(EnumType.STRING)
  private ItemSellStatus itemSellStatus; //상품 판매 상태
  
  public void updateItem(ItemFormDto itemFormDto){
    this.itemNm = itemFormDto.getItemNm();
    this.price = itemFormDto.getPrice();
    this.stockNumber = itemFormDto.getStockNumber();
    this.itemDetail = itemFormDto.getItemDetail();
    this.itemSellStatus = itemFormDto.getItemSellStatus();
  }
}
