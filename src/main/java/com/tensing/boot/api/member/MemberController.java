package com.tensing.boot.api.member;

import com.tensing.boot.api.member.payload.MemberDto;
import com.tensing.boot.api.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequestMapping("/api/members")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> createMember(@RequestBody @Valid MemberDto.MemberRequest postRequest) {
        final var memberId = memberService.createMember(postRequest);
        return ResponseEntity.created(URI.create("/api/members/" + memberId)).build();
    }

}
