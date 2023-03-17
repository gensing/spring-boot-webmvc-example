package com.tensing.boot.api.post.controller;

import com.tensing.boot.api.post.dto.PostDto;
import com.tensing.boot.api.post.service.PostService;
import com.tensing.boot.config.OpenApiConfiguration;
import com.tensing.boot.security.code.RoleCode;
import com.tensing.boot.security.dto.SecurityDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping("")
    @Secured(value = RoleCode.USER_VALUE)
    @SecurityRequirement(name = OpenApiConfiguration.API_SCHEME_NAME_001)
    public ResponseEntity<Void> create(@AuthenticationPrincipal SecurityDto.UserInfo sessionInfo, @RequestBody @Valid PostDto.PostRequest postRequest) {
        final long postId = postService.insert(postRequest, sessionInfo);
        return ResponseEntity.created(URI.create("/api/posts/" + postId)).build();
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto.PostResponse> getList() {
        return postService.getList();
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto.PostResponse get(@PathVariable long postId) {
        return postService.get(postId);
    }

    @PutMapping("/{postId}")
    @Secured(value = RoleCode.USER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = OpenApiConfiguration.API_SCHEME_NAME_001)
    public void put(@AuthenticationPrincipal SecurityDto.UserInfo sessionInfo, @PathVariable long postId, @RequestBody @Valid PostDto.PostPutRequest postPutRequest) {
        postService.put(postId, postPutRequest, sessionInfo);
    }


    @DeleteMapping("/{postId}")
    @Secured(value = RoleCode.USER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = OpenApiConfiguration.API_SCHEME_NAME_001)
    public void delete(@AuthenticationPrincipal SecurityDto.UserInfo sessionInfo, @PathVariable long postId) {
        postService.delete(postId, sessionInfo);
    }

}
