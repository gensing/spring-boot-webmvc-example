package com.tensing.boot.application.member.dao;

import com.tensing.boot.application.member.model.data.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberEntityRepository extends JpaRepository<MemberEntity, Long>, MemberEntityDao {

    Optional<MemberEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
