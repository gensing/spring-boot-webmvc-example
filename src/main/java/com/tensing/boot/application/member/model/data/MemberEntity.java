package com.tensing.boot.application.member.model.data;

import com.tensing.boot.common.model.vo.entity.BaseEntity;
import com.tensing.boot.global.filters.security.model.code.RoleCode;
import com.tensing.boot.global.filters.security.model.code.RoleConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "member")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection(targetClass = RoleCode.class, fetch = FetchType.EAGER)
    @Convert(converter = RoleConverter.class)
    @CollectionTable(name = "member_role", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "role_desc", nullable = false)
    private Set<RoleCode> roles;
}
