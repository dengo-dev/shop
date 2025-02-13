package com.shop.config;

import com.shop.service.MemberService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@Log4j2
public class SecurityConfig  {
  
  @Autowired
  MemberService memberService;
  
  
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
            .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
            .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
            .requestMatchers("/admin/**").hasRole("ADMIN") //요청하는 계정이 AMIND권한이여야 한다
            .anyRequest()
            .authenticated()
        ).formLogin(formLoginCustomizer -> formLoginCustomizer
            .loginPage("/members/login")
            .defaultSuccessUrl("/")
            .usernameParameter("email")  //로그인 시 사용할 파라미터 이름
            .failureHandler(new CustomAuthenticationFailureHandler())  //로그인 실패시
        ).logout( logoutCustomizer -> logoutCustomizer
            .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
            .logoutSuccessUrl("/")
        
        )
        .build()
        ;
  }

  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
}