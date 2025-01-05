package com.shop.repository;

import com.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  
  //회원 cart찾는 쿼리 메소드 추가
  Cart findByMemberId(Long memberId);
}
