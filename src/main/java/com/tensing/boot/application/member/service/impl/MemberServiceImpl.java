package com.tensing.boot.application.member.service.impl;

import com.tensing.boot.application.member.dao.MemberEntityRepository;
import com.tensing.boot.application.member.model.MemberMapper;
import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.model.data.MemberEntity;
import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.global.advice.exception.exception.BusinessException;
import com.tensing.boot.global.advice.exception.model.code.ErrorCode;
import com.tensing.boot.global.filters.security.model.code.RoleCode;
import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberEntityRepository memberEntityRepository;
    private final MemberMapper memberMapper;

    @Transactional
    @Override
    public MemberDto.MemberResponse createMember(MemberDto.MemberRequest postRequest) {

        final var memberEntity = memberMapper.toMember(postRequest, Set.of(RoleCode.USER));

        if (memberEntityRepository.existsByUsername(memberEntity.getUsername()))
            throw new BusinessException(ErrorCode.DUPLICATION_USER);

        final var savedMemberEntity = memberEntityRepository.save(memberEntity);

        return memberMapper.toMemberResponse(savedMemberEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberDto.MemberResponse findMember(long memberId, SecurityDto.UserInfo sessionInfo) {

        if (!sessionInfo.checkId(memberId))
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);

        final var memberEntity = memberEntityRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA));

        return memberMapper.toMemberResponse(memberEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public MemberEntity findMember(String username, String password) {

        final var memberEntity = memberEntityRepository.findByUsername(username);
        final var isValidPassword = (memberEntity.isPresent() && passwordEncoder.matches(password, memberEntity.get().getPassword()));
        return isValidPassword ? memberEntity.get() : null;
    }

}
