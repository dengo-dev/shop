package com.shop.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) //Auditing적용 위해 어노테이션 추가
@MappedSuperclass  //공통 매핑 정보 필요시 사용하는 어노테이션, 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
@Getter
@Setter
public abstract class BaseTimeEntity {
  
  @CreatedDate   //엔티티 생성시 시간 자동 저장
  @Column(updatable = false)
  private LocalDateTime regTime;
  
  @LastModifiedDate //엔티티 값 변경시 시간 자동 저장
  private LocalDateTime updateTime;
}
