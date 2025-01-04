package com.shop.controller;


import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
public class ItemController {
  
  
  private final ItemService itemService;
  
  public ItemController(ItemService itemService) {
    this.itemService = itemService;
  }
  
  @GetMapping("/admin/item/new")
  public String itemForm(Model model) {
    model.addAttribute("itemFormDto", new ItemFormDto());
    return "/item/itemForm";
  }
  
  @PostMapping("/admin/item/new")
  public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
    
    if (bindingResult.hasErrors()) {
      return "item/itemForm";
    }
    
    if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
      model.addAttribute("errorMessage", "첫 번쨰 상품 이미지는 필수 입력 값입니다.");
      return "item/itemForm";
    }
    
    try {
      itemService.saveItem(itemFormDto, itemImgFileList);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
      return "item/itemForm";
    }
    return "redirect:/";
  }
  
  @GetMapping(value = "/admin/item/{itemId}")
  public String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
    try {
      ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
      model.addAttribute("itemFormDto", itemFormDto);
    } catch (EntityNotFoundException e) {
      model.addAttribute("errorMessagee", "존재하지 않는 상품입니다.");
      model.addAttribute("itemFormDto", new ItemFormDto());
      return "/item/itemForm";
    }
    return "item/itemForm";
  }
  
  @PostMapping("/admin/item/{itemId}")
  public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model) {
    if (bindingResult.hasErrors()) {
      return "item/itemForm";
    }
    if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
      model.addAttribute("errorMessage", "첫 번쨰 상품 이미지는 필수 입력 값입니다.");
      return "item/itemForm";
    }
    try {
      itemService.updateItem(itemFormDto, itemImgFileList);
    } catch (Exception e) {
      model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
      return "item/itemForm";
    }
    return "redirect:/";
  }
  
  @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
  public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
    
    Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
    Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
    
    model.addAttribute("items", items);
    //페이지 전환시 기존 검색 조건을 유지하려고 뷰에 다시 전달.
    model.addAttribute("itemSearchDto", itemSearchDto);
    model.addAttribute("maxPage", 5);
    
    return "item/itemMng";
  }
}
