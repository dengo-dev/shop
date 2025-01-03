package com.shop.dto;


import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
  
  
  private Long id;
  
  @NotBlank(message = "상품명은 필수 입력 값입니다.")
  private String itemNm;

  @NotNull(message = "가격은 필수 입력 값입니다.")
  private Integer price;
  
  @NotBlank(message = "이름은 필수 입력 값입니다.")
  private String itemDetail;
  
  @NotNull(message = "재고는 필수 입력 값입니다.")
  private Integer stockNumber;
  
  private ItemSellStatus itemSellStatus;
  
  //상품을 수정할 떄 상품 이미지 정보 저장
  private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
  
  
  //상품 이미지 아이디 저장. 상품 등록 시 아직 상품의 이미지를 저장하지 않았기 때문에 아무 값도 들어가 있지 않고
  //수정시에 이미지 아이디를 담아둘 용도로 사용
  private List<Long> itemImgIds = new ArrayList<>();
  
  private static ModelMapper modelMapper = new ModelMapper();
  
  
  //modelMapper를 이용해 엔티티 객체와 dto객체간의 데이터를 복사해서 복사한 객체를 반환해주는 메소드 아래도 동일.
  public Item createItem() {
    return modelMapper.map(this, Item.class);
  }
  
  public static ItemFormDto of(Item item) {
    return modelMapper.map(item, ItemFormDto.class);
  }
}
