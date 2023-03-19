package com.tensing.boot.api.post.service;

import com.tensing.boot.api.post.dto.PostDto;
import com.tensing.boot.api.post.entity.Post;
import com.tensing.boot.api.post.dao.PostRepository;
import com.tensing.boot.global.exception.code.ErrorCode;
import com.tensing.boot.global.exception.exception.BusinessException;
import com.tensing.boot.global.security.dto.SecurityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public long insert(PostDto.PostRequest postRequest, SecurityDto.UserInfo sessionInfo) {
        final Post post = postRequest.to(sessionInfo.getId());
        final Post newPost = postRepository.save(post);
        return newPost.getId();
    }

    @Override
    public List<PostDto.PostResponse> getList() {
        return postRepository.findAll().stream().map(post -> PostDto.PostResponse.of(post)).toList();
    }

    @Override
    public PostDto.PostResponse get(long postId) {
        final Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMON_ERROR));
        return PostDto.PostResponse.of(post);
    }

    @Override
    public void put(long postId, PostDto.PostPutRequest postPutRequest, SecurityDto.UserInfo sessionInfo) {

    }

    @Override
    public void delete(long postId, SecurityDto.UserInfo sessionInfo) {

    }
}
