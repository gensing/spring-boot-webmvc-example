package com.tensing.boot.application.post.controller;

import com.tensing.boot.application.post.model.dto.PostDto;
import com.tensing.boot.application.post.model.dto.SearchCondition;
import com.tensing.boot.application.post.service.PostService;
import com.tensing.boot.config.OpenApiConfiguration;
import com.tensing.boot.global.security.model.code.RoleCode;
import com.tensing.boot.global.security.model.dto.SecurityDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RequestMapping("/api/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;


    @PostMapping("")
    @Secured(RoleCode.USER_VALUE)
    @SecurityRequirement(name = OpenApiConfiguration.API_SCHEME_NAME_001)
    public ResponseEntity<PostDto.PostResponse> create(@AuthenticationPrincipal SecurityDto.UserInfo sessionInfo, @RequestBody @Valid PostDto.PostRequest postRequest, UriComponentsBuilder b) {
        var newPost = postService.insert(postRequest, sessionInfo);
        var uriComponents = b.path("/api/posts/{id}").buildAndExpand(newPost.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(newPost);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto.PostResponse> getList(@PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.getList(pageable);
    }

    @GetMapping("/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto.PostResponse get(@PathVariable long postId) {
        return postService.get(postId);
    }

    @PutMapping("/{postId}")
    @Secured(RoleCode.USER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = OpenApiConfiguration.API_SCHEME_NAME_001)
    public PostDto.PostResponse put(@AuthenticationPrincipal SecurityDto.UserInfo sessionInfo, @PathVariable long postId, @RequestBody @Valid PostDto.PostPutRequest postPutRequest) {
        return postService.update(postId, postPutRequest, sessionInfo);
    }

    @DeleteMapping("/{postId}")
    @Secured(RoleCode.USER_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = OpenApiConfiguration.API_SCHEME_NAME_001)
    public void delete(@AuthenticationPrincipal SecurityDto.UserInfo sessionInfo, @PathVariable long postId) {
        postService.delete(postId, sessionInfo);
    }

    @GetMapping("/_search")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto.PostResponse> search(SearchCondition searchCondition, @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return postService.search(searchCondition, pageable);
    }

}
