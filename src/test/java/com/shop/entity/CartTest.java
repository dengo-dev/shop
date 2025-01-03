package com.shop.entity;


import com.shop.dto.MemberFormDto;
import com.shop.repository.CartRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class CartTest {
  
  @Autowired
  CartRepository cartRepository;
  
  @Autowired
  PasswordEncoder passwordEncoder;
  
  @Autowired
  MemberRepository memberRepository;
  
  @PersistenceContext
  EntityManager em;
  
  public Member createMember() {
    MemberFormDto memberFormDto = new MemberFormDto();
    memberFormDto.setEmail("test@email.com");
    memberFormDto.setName("홍길동");
    memberFormDto.setAddress("서울시 마포구 망원동");
    memberFormDto.setPassword("1234");
    return Member.createMember(memberFormDto, passwordEncoder);
  }
  
  
  @Test
  @DisplayName("장바구니 회원 엔티티 매핑 조회 테스트")
  public void findCartAndMemberTest() {
    Member member = createMember();
    memberRepository.save(member);
    
    Cart cart = new Cart();
    cart.setMember(member);
    cartRepository.save(cart);
    
    em.flush();
    //영속성 컨텍스트에 엔티티가 없을 경우 db를 조회함. 실제 db에서 장바구니 엔티티를 갖고 올떄 회원 엔티티도 갖고 오는지 보기위해 영속석 컨텍스트를 비워줌
    em.clear();
    
    Cart savedCart = cartRepository.findById(cart.getId())
        .orElseThrow(EntityNotFoundException::new);
    
    assertEquals(savedCart.getMember().getId(), member.getId());
  }
}
