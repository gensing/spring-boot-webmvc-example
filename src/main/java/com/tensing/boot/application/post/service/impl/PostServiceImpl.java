package com.tensing.boot.application.post.service.impl;

import com.tensing.boot.application.post.dao.PostDocumentRepository;
import com.tensing.boot.application.post.dao.PostRepository;
import com.tensing.boot.application.post.model.PostMapper;
import com.tensing.boot.application.post.model.dto.PostDto;
import com.tensing.boot.application.post.model.dto.SearchCondition;
import com.tensing.boot.application.post.model.vo.entity.Post;
import com.tensing.boot.application.post.service.PostService;
import com.tensing.boot.global.advice.exception.exception.BusinessException;
import com.tensing.boot.global.advice.exception.model.code.ErrorCode;
import com.tensing.boot.global.filters.security.model.dto.SecurityDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostDocumentRepository postDocumentRepository;

    private final PostMapper postMapper;


    @Transactional(readOnly = true)
    @Override
    public List<PostDto.PostResponse> search(SearchCondition searchCondition, Pageable pageable) {
        final var postDocuments = postDocumentRepository.search(searchCondition, pageable);
        return postDocuments.stream().map(postMapper::toPostResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostDto.PostResponse> getList(Pageable pageable) {
        final var posts = postRepository.findAll();
        return posts.stream().map(postMapper::toPostResponse).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public PostDto.PostResponse get(long postId) {
        final Post post = postRepository.findById(postId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA));
        return postMapper.toPostResponse(post);
    }

    @Transactional
    @Override
    public long insert(PostDto.PostRequest postRequest, SecurityDto.UserInfo sessionInfo) {
        final Post post = postMapper.toPost(postRequest, sessionInfo.getId());
        final Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    @Transactional
    @Override
    public void update(long postId, PostDto.PostPutRequest postPutRequest, SecurityDto.UserInfo sessionInfo) {

        final Post savedPost = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA));

        if (!sessionInfo.checkId(savedPost.getMember().getId())) {
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        // dirty check vs save or queryDsl ??
        savedPost.update(postMapper.toPost(postPutRequest));
    }

    @Transactional
    @Override
    public void delete(long postId, SecurityDto.UserInfo sessionInfo) {

        final Post saved = postRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA));

        if (!sessionInfo.checkId(saved.getMember().getId())) {
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        postRepository.deleteById(saved.getId());
        // 색인 비용은 크기 때문에 모아서 색인하지만, 삭제 비용은 크지 않을 것으로 생각하여 실시간으로!
        // 만약 logstash 에서 색인을 위한 select 시점과 로그스태시에서 엘라스틱서치로 색인 시점 사이에 삭제가 되어 싱크가 깨지는지 확인 필요
        postDocumentRepository.deleteById(postId);
    }

}
