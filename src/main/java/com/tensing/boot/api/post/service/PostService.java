package com.tensing.boot.api.post.service;

import com.tensing.boot.api.post.dto.PostDto;
import com.tensing.boot.global.security.dto.SecurityDto;

import java.util.List;

public interface PostService {
    long insert(PostDto.PostRequest postRequest, SecurityDto.UserInfo sessionInfo);

    List<PostDto.PostResponse> getList();

    PostDto.PostResponse get(long postId);

    void put(long postId, PostDto.PostPutRequest postPutRequest, SecurityDto.UserInfo sessionInfo);

    void delete(long postId, SecurityDto.UserInfo sessionInfo);
}
