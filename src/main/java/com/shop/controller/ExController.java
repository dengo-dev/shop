package com.shop.controller;


import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Controller
@RequestMapping("/thy")
public class ExController {
  
  @GetMapping("/ex01")
  public String thymeleafEx01(Model model) {
    model.addAttribute("data", "타임리프 예제");
    return "thymeleafEx/Ex01";
  }
  
  @GetMapping("/ex02")
  public String thymeleafEx02(Model model) {
    
    ItemDto itemDto = new ItemDto();
    itemDto.setItemDetail("상품 상세 설명");
    itemDto.setItemNm("테스트 상품1");
    itemDto.setPrice(10000);
    itemDto.setRegTime(LocalDateTime.now());
    
    model.addAttribute("itemDto", itemDto);
    return "thymeleafEx/Ex02";
  }
  
  @GetMapping("/ex03")
  public String thymeleafEx03(Model model) {
    
    ArrayList<ItemDto> itemDtoList = new ArrayList<>();
    
    for (int i = 0; i <= 10; i++) {
      ItemDto itemDto = new ItemDto();
      itemDto.setItemDetail("상품 상세 설명"+i);
      itemDto.setItemNm("테스트 상품"+i);
      itemDto.setPrice(10000*i);
      itemDto.setRegTime(LocalDateTime.now());
      itemDtoList.add(itemDto);
    }
    model.addAttribute("itemDtoList", itemDtoList);
    return "thymeleafEx/Ex03";
  }
}



















