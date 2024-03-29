package com.tensing.boot.application.post.service.impl;

import com.tensing.boot.application.post.dao.PostDocumentRepository;
import com.tensing.boot.application.post.dao.PostEntityRepository;
import com.tensing.boot.application.post.model.PostMapper;
import com.tensing.boot.application.post.model.dto.PostDto;
import com.tensing.boot.application.post.model.dto.SearchCondition;
import com.tensing.boot.application.post.service.PostService;
import com.tensing.boot.global.exception.exception.BusinessException;
import com.tensing.boot.global.exception.model.code.ErrorCode;
import com.tensing.boot.global.security.model.dto.SecurityDto;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final PostEntityRepository postEntityRepository;
    private final PostDocumentRepository postDocumentRepository;
    private final PostMapper postMapper;


    @Transactional
    @Override
    public PostDto.PostResponse insert(PostDto.PostRequest postRequest, SecurityDto.UserInfo sessionInfo) {
        final var requestPostEntity = postMapper.toPost(postRequest, sessionInfo.getId());
        final var savedPostEntity = postEntityRepository.save(requestPostEntity);
        return postMapper.toPostResponse(savedPostEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostDto.PostResponse> getList(Pageable pageable, SearchCondition searchCondition) {
        return (searchCondition == null || StringUtils.isEmpty(searchCondition.getSearch()))
                ? postEntityRepository.findList(pageable)
                : postDocumentRepository.search(searchCondition, pageable).stream().map(postMapper::toPostResponse).toList();
    }

    @Cacheable(value = "postResponseCache", key = "#postId")
    @Transactional(readOnly = true)
    @Override
    public PostDto.PostResponse get(long postId) {
        final var postEntity = postEntityRepository.findOne(postId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA));
        return postEntity;
    }

    @CachePut(value = "postResponseCache", key = "#postId")
    @Transactional
    @Override
    public PostDto.PostResponse update(long postId, PostDto.PostPutRequest postPutRequest, SecurityDto.UserInfo sessionInfo) {

        final var savedPostEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA));

        if (!sessionInfo.checkId(savedPostEntity.getMemberEntity().getId())) {
            log.info("invalid requestId - sessionId: {}, request id: {}", sessionInfo.getId(), savedPostEntity.getMemberEntity().getId());
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        // dirty check vs save or queryDsl ??
        savedPostEntity.update(postMapper.toPost(postPutRequest));

        return postMapper.toPostResponse(savedPostEntity);
    }

    @CacheEvict(value = "postResponseCache", key = "#postId")
    @Transactional
    @Override
    public void delete(long postId, SecurityDto.UserInfo sessionInfo) {

        final var savedPostEntity = postEntityRepository.findById(postId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_DATA));

        if (!sessionInfo.checkId(savedPostEntity.getMemberEntity().getId())) {
            throw new BusinessException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        postEntityRepository.deleteById(savedPostEntity.getId());
        // 색인 비용은 크기 때문에 모아서 색인하지만, 삭제 비용은 크지 않을 것으로 생각하여 실시간으로!
        // 만약 logstash 에서 색인을 위한 select 시점과 로그스태시에서 엘라스틱서치로 색인 시점 사이에 삭제가 되어 싱크가 깨지는지 확인 필요
        postDocumentRepository.deleteById(savedPostEntity.getId());
    }

}
