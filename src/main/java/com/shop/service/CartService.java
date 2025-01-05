package com.shop.service;


import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.repository.CartItemRepository;
import com.shop.repository.CartRepository;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
  
  private final ItemRepository itemRepository;
  private final MemberRepository memberRepository;
  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  
  public Long addCart(CartItemDto cartItemDto, String email) {
    //장바구니 담을 엔티티 조회
    Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
    
    //현재 로그인한 회원 엔티티 조회
    Member member = memberRepository.findByEmail(email);
    
    //현재 로그인한 회원의 장바구니 엔티티 조회
    Cart cart = cartRepository.findByMemberId(member.getId());
    
    //상품을 처음 장바구니에 담을 경우 해당 회원의 장부구니 엔티티 생성
    if (cart == null) {
      cart = Cart.createCart(member);
      cartRepository.save(cart);
    }
    
    //현재 상품이 장바구니에 이미 들어가 있는지 조회
    CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
    
    if (savedCartItem != null) {
      //만약 장바구니에 들어가 있다면 현재 장바구니에 담으 수량만큼 더함
      savedCartItem.addCount(cartItemDto.getCount());
      return savedCartItem.getId();
    } else {
      //장바구니 엔티티, 상품 엔티티, 장바구니에 담을 수량을 이용해 CartItem엔티티를 생성
      CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
      //장바구니에 들어갈 상품 저장
      cartItemRepository.save(cartItem);
      return cartItem.getId();
    }
  }
  
  @Transactional(readOnly = true)
  public List<CartDetailDto> getCartList(String email) {
    List<CartDetailDto> cartDetailDtoList = new ArrayList<>();
    
    Member member = memberRepository.findByEmail(email);
    Cart cart = cartRepository.findByMemberId(member.getId());  //현재 로그인한 회원 장바구니 엔티티 조회
    if (cart == null) {  //장바구니에 상품을 한 번도 안 담았을 경우 장바구니 엔티티가 없으르모 빈 리스트 반환
      return cartDetailDtoList;
    }
    cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
    return cartDetailDtoList;
  }
  
  @Transactional(readOnly = true)
  public boolean validateCartItem(Long cartItemId, String email) {
    Member curMember = memberRepository.findByEmail(email);
    
    CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
    
    //장바구니 상품을 저장한 회원을 조회
    Member savedMember = cartItem.getCart().getMember();
    
    if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())) {
      return false;
    }
    return true;
  }
  
  public void updateCartItemCart(Long cartItemId, int count) {
    CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
    
    cartItem.updateCount(count);
    
  }
}
