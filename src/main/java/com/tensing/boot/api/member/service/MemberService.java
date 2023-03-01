package com.tensing.boot.api.member.service;

import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.api.member.payload.MemberDto;

public interface MemberService {
    void signup(MemberDto.MemberRequest postRequest);

    Member getMemberByUserNameAndPassword(String username, String password);
}
