package com.tensing.boot.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class) // @CreatedDate, @LastModifiedDate 사용을 위한 설정
public abstract class BaseEntity {

    // 상속보다는 위임이 좋다.
    // @MappedSuperclass (상속) vs @Embedded (위임)
    // jpql 사용시 -> u.timestamped.createdDate vs u.createdDate, @MappedSuperclass 사용 편이성이 좋다.

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp // hibernate
    //@CreatedDate
    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp // hibernate
    //@LastModifiedDate
    @Column(name = "update_at", nullable = false)
    private LocalDateTime lastModifiedDate;
}