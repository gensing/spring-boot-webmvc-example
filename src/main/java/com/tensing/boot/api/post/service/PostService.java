package com.tensing.boot.api.post.service;

import com.tensing.boot.api.post.model.vo.document.PostDocument;
import com.tensing.boot.api.post.model.dto.PostDto;
import com.tensing.boot.api.post.model.dto.SearchCondition;
import com.tensing.boot.global.security.dto.SecurityDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    long insert(PostDto.PostRequest postRequest, SecurityDto.UserInfo sessionInfo);

    List<PostDto.PostResponse> getList(Pageable pageable);

    PostDto.PostResponse get(long postId);

    void put(long postId, PostDto.PostPutRequest postPutRequest, SecurityDto.UserInfo sessionInfo);

    void delete(long postId, SecurityDto.UserInfo sessionInfo);

    List<PostDocument> search(SearchCondition searchCondition, Pageable pageable);
}
