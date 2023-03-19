package com.tensing.boot.api.member.service;

import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.api.member.dto.MemberDto;
import com.tensing.boot.api.member.dao.MemberRepository;
import com.tensing.boot.global.exception.code.ErrorCode;
import com.tensing.boot.global.exception.exception.BusinessException;
import com.tensing.boot.global.security.code.RoleCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public long createMember(MemberDto.MemberRequest postRequest) {

        if (memberRepository.existsByUsername(postRequest.getUsername()))
            throw new BusinessException(ErrorCode.DUPLICATION_USER);

        final var member = Member.builder()
                .username(postRequest.getUsername())
                .email(postRequest.getEmail())
                .password(passwordEncoder.encode(postRequest.getPassword()))
                .roles(Set.of(RoleCode.USER))
                .build();

        return memberRepository.save(member).getId();
    }

    @Override
    public Member findMember(long sessionId, long memberId) {
        if (sessionId != memberId) throw new AccessDeniedException("권한이 없습니다.");
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_MEMBER));
    }

    @Override
    public Member findMember(String username, String password) {
        final var member = memberRepository.findByUsername(username);
        // 체크 필요
        return (member.isPresent() && passwordEncoder.matches(password, member.get().getPassword()))
                ? member.get()
                : null;
    }


}
