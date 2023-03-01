package com.tensing.boot.api.member.service;

import com.tensing.boot.api.member.entity.Member;
import com.tensing.boot.api.member.payload.MemberDto;
import com.tensing.boot.api.member.repository.MemberRepository;
import com.tensing.boot.security.code.RoleCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public void signup(MemberDto.MemberRequest postRequest) {
        final var member = Member.builder()
                .username(postRequest.getUsername())
                .password(passwordEncoder.encode(postRequest.getPassword()))
                .roles(Stream.of(RoleCode.USER).collect(Collectors.toUnmodifiableSet()))
                .build();
        memberRepository.save(member);
    }

    @Override
    public Member getMemberByUserNameAndPassword(String username, String password) {
        final var member = memberRepository.findByUsername(username).orElse(null);
        if (member == null) return null;
        return passwordEncoder.matches(password, member.getPassword()) ? member : null;
    }
}
