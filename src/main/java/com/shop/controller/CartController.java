package com.shop.controller;


import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.dto.CartOrderDto;
import com.shop.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
  
  
  private final CartService cartService;
  
  
  @PostMapping("/cart")
  public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {
    
    
    //장바구니에 담을 상품 정보를 받는 cartItemDto객체에 데이터 바인딩 시 에러가 있는지 검사
    if (bindingResult.hasErrors()) {
      StringBuilder sb = new StringBuilder();
      List<FieldError> fieldErrors = bindingResult.getFieldErrors();
      for (FieldError fieldError : fieldErrors) {
        sb.append(fieldError.getDefaultMessage());
      }
      return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
    }
    
    //현재 로그인한 회원의 이메일 정보를 변수에 저장
    String email = principal.getName();
    Long cartItemId;
    
    try {
      //화면으로 부터 넘어온 장바구니에 담을 상품 정보와 현재 로그인한 회원의 이메일 정보를 이용해
      //장바구니에 상품을 담는 로직을 호출
      cartItemId = cartService.addCart(cartItemDto, email);
    } catch (Exception e) {
      return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
  }
  
  @GetMapping("/cart")
  public String orderHist(Principal principal, Model model) {
    List<CartDetailDto> cartDetailDtoList = cartService.getCartList(principal.getName()); //현재 로그인한 사용자의 이메일 정보를 이용해 장바구니에 담겨있는 상품조회
    model.addAttribute("cartItems", cartDetailDtoList);
    return "cart/cartList";
  }
  
  @PatchMapping("/cartItem/{cartItemId}")
  public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal) {
    if (count <= 0) {
      return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
      
      //수정 권한 체크
    } else if (!cartService.validateCartItem(cartItemId, principal.getName())) {
      return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
    
    //장바구니 개수 업데이트
    cartService.updateCartItemCart(cartItemId, count);
    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
  }
  
  @DeleteMapping("/cartItem/{cartItemId}")
  public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal) {
    
    
    //수정 권한 체크
    if (!cartService.validateCartItem(cartItemId, principal.getName())) {
      return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
    }
    cartService.deleteCartItem(cartItemId);
    return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
  }
  
  @PostMapping("/cart/orders")
  public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal) {
    List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();
    
    
    //주문 상품을 선택했는지 체크
    if (cartOrderDtoList == null || cartOrderDtoList.size() == 0) {
      return new ResponseEntity<String>("주문할 상품을 선택해주세요", HttpStatus.FORBIDDEN);
    }
    
    //주문 권한 체크
    for (CartOrderDto cartOrder : cartOrderDtoList) {
      if (!cartService.validateCartItem(cartOrder.getCartItemId(), principal.getName())) {
        return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
      }
    }
    Long orderId = cartService.orderCartItem(cartOrderDtoList, principal.getName());
    return new ResponseEntity<Long>(orderId, HttpStatus.OK);
  }
  
}
