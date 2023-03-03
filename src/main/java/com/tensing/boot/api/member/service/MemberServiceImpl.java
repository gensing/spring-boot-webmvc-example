package com.tensing.boot.api.member.service;

import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.api.member.payload.MemberDto;
import com.tensing.boot.api.member.repository.MemberRepository;
import com.tensing.boot.exception.code.ErrorCode;
import com.tensing.boot.exception.exception.BusinessException;
import com.tensing.boot.security.code.RoleCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
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
    public Optional<Member> findMember(String username, String password) {
        final var member = memberRepository.findByUsername(username);
        return (member.isPresent() & passwordEncoder.matches(password, member.get().getPassword()))
                ? member
                : Optional.of(null);
    }
}
