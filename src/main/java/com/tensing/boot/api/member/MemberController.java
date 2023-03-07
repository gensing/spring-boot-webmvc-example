package com.tensing.boot.api.member;

import com.tensing.boot.api.member.payload.MemberDto;
import com.tensing.boot.api.member.service.MemberService;
import com.tensing.boot.config.OpenApiConfiguration;
import com.tensing.boot.security.code.RoleCode;
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
    public MemberDto.MemberGetResponse getMember(@AuthenticationPrincipal long sessionId, @PathVariable long memberId) {
        // @AuthenticationPrincipal 객체를 class type 으로 받아오도록 수정 필요.
        // 현재는 권한 자체가 없을시 @Secured 보다 먼저  @AuthenticationPrincipal 가 long type 에 null 을 리턴하여 @Secured 처리전 500 에러 발생
        final var member = memberService.findMember(sessionId, memberId);
        return MemberDto.MemberGetResponse.of(member);
    }

}
