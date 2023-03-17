package com.tensing.boot.api.post.entity;

import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.common.entity.BaseDate;
import com.tensing.boot.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "Post")
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

    @Column(name = "username", unique = true, nullable = false)
    private String title;

    @Column(name = "email", unique = true, nullable = false)
    private String body;

}
