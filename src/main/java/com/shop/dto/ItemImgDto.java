package com.shop.dto;


import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
  
  private Long id;
  
  private String imgName;
  
  private String oriImgName;
  
  private String imgUrl;
  
  private String repImgYn;
  
  private static ModelMapper modelMapper = new ModelMapper(); //멤버 변수로 모델 객체 추가
  
  
  //ItemImg엔티티 객체를 파라미터로 받아서 ItemImg객체의 자료형과 멤버변수의 이름이 같을 떄 ItemImgDto로 값을 복사해서 반환
  //static 선언으로 ItemImgDto객체를 생성하지 않아도 호출 될 수 있도록 설정
  public static ItemImgDto of(ItemImg itemImg) {
    return modelMapper.map(itemImg, ItemImgDto.class);
  }
}
