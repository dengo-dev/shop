package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
  
  List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
  
  ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn); //대포 이미지 찾는 쿼리 메소드 추가

}
