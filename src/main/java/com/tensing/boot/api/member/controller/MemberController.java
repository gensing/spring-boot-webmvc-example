package com.tensing.boot.api.member.controller;

import com.tensing.boot.api.member.model.dto.MemberDto;
import com.tensing.boot.api.member.service.MemberService;
import com.tensing.boot.global.config.OpenApiConfiguration;
import com.tensing.boot.global.security.code.RoleCode;
import com.tensing.boot.global.security.dto.SecurityDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberDto.MemberRequest postRequest) {
        final var memberId = memberService.createMember(postRequest);
        return ResponseEntity.created(URI.create("/api/members/" + memberId)).build();
    }

    @GetMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    @Secured(value = RoleCode.USER_VALUE)
    @SecurityRequirement(name = OpenApiConfiguration.API_SCHEME_NAME_001)
    public MemberDto.MemberGetResponse getMember(@AuthenticationPrincipal SecurityDto.UserInfo sessionInfo, @PathVariable long memberId) {
        final var member = memberService.findMember(sessionInfo.getId(), memberId);
        return MemberDto.MemberGetResponse.of(member);
    }

}
