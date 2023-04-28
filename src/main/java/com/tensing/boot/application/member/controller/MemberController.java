package com.tensing.boot.application.member.controller;

import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.service.MemberService;
import com.tensing.boot.global.security.model.code.RoleCode;
import com.tensing.boot.global.security.model.dto.SecurityDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("")
    public ResponseEntity<MemberDto.MemberResponse> createMember(@RequestBody @Valid MemberDto.MemberRequest postRequest) {
        final var memberResponse = memberService.createMember(postRequest);
        return ResponseEntity.created(URI.create("/api/members/" + memberResponse.getId())).body(memberResponse);
    }

    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(value = RoleCode.USER_VALUE)
    public MemberDto.MemberResponse getMember(@AuthenticationPrincipal SecurityDto.UserInfo sessionInfo, @PathVariable long memberId) {
        final var member = memberService.findMember(memberId, sessionInfo);
        return member;
    }

}
