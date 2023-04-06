package com.tensing.boot.application.post.dao.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tensing.boot.application.member.model.data.QMemberEntity;
import com.tensing.boot.application.post.dao.PostEntityDao;
import com.tensing.boot.application.post.model.data.QPostEntity;
import com.tensing.boot.application.post.model.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostEntityDaoImpl implements PostEntityDao {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostDto.PostResponse> findList(Pageable pageable) {

        final var post = QPostEntity.postEntity;
        final var member = QMemberEntity.memberEntity;

        final var query = jpaQueryFactory
                .select(
                        // 리스트에서 필요 없는 정보 제거 필요 ( findOne 필드와 달라질걸 대비해서 따로 사용 )
                        Projections.constructor(PostDto.PostResponse.class
                                , post.id.as(PostDto.PostResponse.Fields.id)
                                , member.username.as(PostDto.PostResponse.Fields.writer)
                                , post.title.as(PostDto.PostResponse.Fields.title)
                                , post.body.as(PostDto.PostResponse.Fields.body)
                                , post.createdDate.as(PostDto.PostResponse.Fields.createdDate)
                                , post.updatedDate.as(PostDto.PostResponse.Fields.updatedDate)
                        )
                )
                .from(post)
                .join(member).on(member.id.eq(post.memberEntity.id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        return query.fetch();
    }

    @Override
    public Optional<PostDto.PostResponse> findOne(long postId) {

        final var post = QPostEntity.postEntity;
        final var member = QMemberEntity.memberEntity;

        final var query = jpaQueryFactory
                .select(
                        Projections.constructor(PostDto.PostResponse.class
                                , post.id.as(PostDto.PostResponse.Fields.id)
                                , member.username.as(PostDto.PostResponse.Fields.writer)
                                , post.title.as(PostDto.PostResponse.Fields.title)
                                , post.body.as(PostDto.PostResponse.Fields.body)
                                , post.createdDate.as(PostDto.PostResponse.Fields.createdDate)
                                , post.updatedDate.as(PostDto.PostResponse.Fields.updatedDate)
                        )
                )
                .from(post)
                .join(member).on(member.id.eq(post.memberEntity.id))
                .where(post.id.eq(postId));

        return Optional.of(query.fetchOne());
    }

}
