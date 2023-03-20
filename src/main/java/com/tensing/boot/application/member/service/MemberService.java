package com.tensing.boot.application.member.service;

import com.tensing.boot.application.member.model.vo.entity.Member;
import com.tensing.boot.application.member.model.dto.MemberDto;

public interface MemberService {
    long createMember(MemberDto.MemberRequest postRequest);
    Member findMember(long sessionId,long memberId);
    Member findMember(String username, String password);

}
