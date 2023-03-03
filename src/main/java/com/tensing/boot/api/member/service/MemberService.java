package com.tensing.boot.api.member.service;

import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.api.member.payload.MemberDto;

import java.util.Optional;

public interface MemberService {
    long createMember(MemberDto.MemberRequest postRequest);

    Optional<Member> findMember(String username, String password);
}
