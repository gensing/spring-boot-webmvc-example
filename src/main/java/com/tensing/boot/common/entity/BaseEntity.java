package com.tensing.boot.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class) // @CreatedDate, @LastModifiedDate 사용을 위한 설정
public abstract class BaseEntity {

    // @MappedSuperclass (상속) vs @Embedded (위임) -> 상속보다는 위임이 좋다.
    // but jpql 사용시 -> u.timestamped.createdDate vs u.createdDate, @MappedSuperclass 사용 편이성이 좋다.

    //@CreatedDate
    @CreationTimestamp // hibernate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdDate;


    //@LastModifiedDate
    @UpdateTimestamp // hibernate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedDate;

    //@PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    //@PreUpdate
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}