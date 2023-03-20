package com.tensing.boot.application.member.dao;

import com.tensing.boot.application.member.model.vo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(String id);

    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);
}
