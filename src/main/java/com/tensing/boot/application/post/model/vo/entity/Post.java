package com.tensing.boot.application.post.model.vo.entity;

import com.tensing.boot.application.member.model.vo.entity.Member;
import com.tensing.boot.common.model.vo.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "post")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "body", nullable = false)
    private String body;

//    @Column(name = "isDelete", nullable = false)
//    private Boolean isDelete;
//
//    @PrePersist
//    public void prePersist() {
//        isDelete = isDelete == null ? false : true;
//    }

}
