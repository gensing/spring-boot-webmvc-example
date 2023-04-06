package com.tensing.boot.application.post.dao;

import com.tensing.boot.application.post.model.dto.PostDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostEntityDao {
    List<PostDto.PostResponse> findList(Pageable pageable);

    Optional<PostDto.PostResponse> findOne(long postId);
}
