package com.tensing.boot.api.member;

import com.tensing.boot.api.member.payload.MemberDto;
import com.tensing.boot.api.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@RequestBody @Valid MemberDto.MemberRequest postRequest) {
        memberService.signup(postRequest);
    }

}
