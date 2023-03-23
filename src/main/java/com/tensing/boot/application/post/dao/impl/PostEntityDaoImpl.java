package com.tensing.boot.application.post.dao.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tensing.boot.application.post.dao.PostEntityDao;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostEntityDaoImpl implements PostEntityDao {
    private final JPAQueryFactory jpaQueryFactory;

}
