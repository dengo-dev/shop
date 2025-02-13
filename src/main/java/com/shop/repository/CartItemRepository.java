package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  
  //카트 아이디와 상품 아이디를 통해 상품이 장바구니에 들어있는지 조회
  CartItem findByCartIdAndItemId(Long cartId, Long itemId);
  
  @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
      "from CartItem ci, ItemImg im " +
      "join ci.item i " +
      "where ci.cart.id = :cartId " +
      "and im.item.id = ci.item.id " +  //장바구니에 담겨있는 상품 대표 이미지만 갖고오는 조건문
      "and im.repImgYn = 'Y' " +
      "order by ci.regTime desc"
  )
  List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
