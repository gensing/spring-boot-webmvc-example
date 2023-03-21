package com.tensing.boot.application.member.service;

import com.tensing.boot.application.member.model.vo.entity.Member;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.global.filters.security.model.dto.SecurityDto;

public interface MemberService {
    long createMember(MemberDto.MemberRequest postRequest);

    MemberDto.MemberResponse findMember(long memberId, SecurityDto.UserInfo sessionInfo);

    Member findMember(String username, String password);

}
