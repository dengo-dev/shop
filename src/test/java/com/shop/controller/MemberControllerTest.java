package com.shop.controller;

import com.shop.dto.MemberFormDto;
import com.shop.entity.Member;
import com.shop.service.MemberService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MemberControllerTest {

  @Autowired
  private MemberService memberService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  PasswordEncoder passwordEncoder;

  public Member createMember(String email, String password){
    MemberFormDto memberFormDto = new MemberFormDto();
    memberFormDto.setEmail(email);
    memberFormDto.setName("홍길동");
    memberFormDto.setAddress("서울시 마포구 합정동");
    memberFormDto.setPassword(password);
    Member member = Member.createMember(memberFormDto, passwordEncoder);
    return memberService.saveMember(member);
  }

  @Test
  @DisplayName("로그인 성공 테스트")
  public void loginSuccessTest() throws Exception{
    String email = "test@email.com";
    String password = "1234";
    this.createMember(email, password);
    mockMvc.perform(formLogin().userParameter("email")
                    //가입된 회원 정보로 로그인 되는지 테스트
                    //UserParameter를 이용해 이메일을 아이디로 세탕하고 로그인 URL요청
                    .loginProcessingUrl("/members/login")
                    .user(email).password(password))
            .andExpect(SecurityMockMvcResultMatchers.authenticated());
  }

  @Test
  @DisplayName("로그인 실패 테스트")
  public void loginFailTest() throws Exception{
    String email = "test@email.com";
    String password = "1234";
    this.createMember(email, password);
    mockMvc.perform(formLogin().userParameter("email")
                    .loginProcessingUrl("/members/login")
                    .user(email).password("12345"))
            //회원가입은 정상적이지만 회원가입시 입력한 비밀번호가 아닌 다른 비밀번호로 로그인 시도시
            //인증되지 않은 결과 값이 출력되어 테스트 통과
            .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
  }

}