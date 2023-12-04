package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

//@EntityListeners : 엔티티의 생명주기(생성, 수정, 소멸)에 대한 리스너를 지정
//@Mappedsuperclass : 부모 클래스로만 사용, 실제 클래스와 매핑 x 엔티티 정의
@EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
@Getter@Setter
public abstract class BaseTimeEntity{

    @CreatedDate//Entity 생성, 저장 시 시간 자동 저장
    @Column(updatable = false)//변경 x
    private LocalDateTime regTime;

    @LastModifiedDate//entity 변경 시 시간 자동 저장
    private LocalDateTime updateTime;


}
