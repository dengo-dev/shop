package com.shop.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations="classpath:application-test.properties")
class ItemControllerTest {
  
  @Autowired
  MockMvc mockMvc; //테스트에 필요한 기능만 가지는 가짜 객체, MockMvc를 이용하면 웹 브라우저에서 요청하는 것처럼 테스트 가능

  
  @Test
  @DisplayName("상품 등록 페이지 권한 테스트")
  ////아래 정의된 username,role의 유저가 로그인된 상태로 진행가능하게 하는 어노테이션
  @WithMockUser(username = "admin", roles = "ADMIN")
  public void itemFormTest() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
        .andDo(print()) //콘솔 창 출력
        .andExpect(status().isOk()); //응답 상태 코드 확인.
  }
  
  @Test
  @DisplayName("상품 등록 페이지 일반 회원 접근 테스트")
  @WithMockUser(username = "user", roles = "USER")
  public void itemFormNotAdminTest() throws Exception{
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }
}