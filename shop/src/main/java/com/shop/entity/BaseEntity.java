package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value={AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends BaseTimeEntity {
    @CreatedBy
    private String createBy;

    @LastModifiedBy
    private String modifiedBy;
}
