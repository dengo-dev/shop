package com.shop.service;


import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
  
  private final ItemRepository itemRepository;
  private final ItemImgRepository itemImgRepository;
  @Value("${itemImgLocation}")
  private String itemImgLocation;
  
  private final FileService fileService;
  
  public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
    String oriImgName = itemImgFile.getOriginalFilename();
    String imgName = "";
    String imgUrl = "";
    
    if (!StringUtils.isEmpty(oriImgName)) {
      imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
      imgUrl = "/images/item/" + imgName;
    }
    
    itemImg.updateItemImg(oriImgName, imgName, imgUrl);
    itemImgRepository.save(itemImg);
    
  }
  
}
