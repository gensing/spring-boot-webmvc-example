package com.tensing.boot.application.member.service;

import com.tensing.boot.application.member.model.data.MemberEntity;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.global.security.model.dto.SecurityDto;

public interface MemberService {
    MemberDto.MemberResponse createMember(MemberDto.MemberRequest postRequest);

    MemberDto.MemberResponse findMember(long memberId, SecurityDto.UserInfo sessionInfo);

    MemberEntity findMember(String username, String password);

}
