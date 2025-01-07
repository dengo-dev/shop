package com.shop.service;


import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional //로직처리하다가 에러발생 시 이전 상태로 콜백
@RequiredArgsConstructor
public class MemberService implements UserDetailsService { //MemberService가 UserDetailService구현
  
  private final MemberRepository memberRepository;
  
  public Member saveMember(Member member) {
    validateDuplicateMember(member);
    return memberRepository.save(member);
  }
  
  
  private void validateDuplicateMember(Member member) {
    Member findMember = memberRepository.findByEmail(member.getEmail());
    if (findMember != null) {
      throw new IllegalStateException("이미 가입된 회원입니다.");
    }
  }
  
  
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Member member = memberRepository.findByEmail(email);
    
    if (member == null) {
      throw new UsernameNotFoundException(email);
    }
    
    return User.builder()  //User객체를 반환
        .username(member.getEmail())
        .password(member.getPassword())
        .roles(member.getRole().toString())
        .build();
  }
}
