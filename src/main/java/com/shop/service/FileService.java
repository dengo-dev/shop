package com.shop.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileService {
  
  public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
    UUID uuid = UUID.randomUUID();
    String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    
    String savedFileName = uuid.toString() + extension;
    
    String fileUploadFullUrl = uploadPath + "/" + savedFileName;
    FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
    fos.write(fileData);
    
    fos.close();
    
    return savedFileName;
  }
  
  
  public void deleteFile(String filePath) throws Exception {
    File deleteFIle = new File(filePath);
    
    if (deleteFIle.exists()) {
      deleteFIle.delete();
      log.info("파일을 삭제했습니다");
    } else {
      log.info("파일이 존재하지 않습니다.");
      
    }
  }
}
