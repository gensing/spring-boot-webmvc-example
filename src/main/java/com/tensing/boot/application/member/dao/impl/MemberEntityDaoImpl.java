package com.tensing.boot.application.member.dao.impl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tensing.boot.application.member.dao.MemberEntityDao;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberEntityDaoImpl implements MemberEntityDao {
    private final JPAQueryFactory jpaQueryFactory;
}
