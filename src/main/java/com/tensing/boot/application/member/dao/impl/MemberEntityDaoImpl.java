package com.tensing.boot.application.member.dao.impl;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberEntityDaoImpl {
    private final JPAQueryFactory jpaQueryFactory;
}
