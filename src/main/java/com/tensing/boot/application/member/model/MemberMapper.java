package com.tensing.boot.application.member.model;

import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.model.vo.entity.Member;
import com.tensing.boot.global.filters.security.model.code.RoleCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;


@Mapper(componentModel = "spring")
public abstract class MemberMapper {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "memberRequest.username", target = "username"),
            @Mapping(source = "memberRequest.password", target = "password", qualifiedByName = "encryptPassword"),
            @Mapping(source = "memberRequest.email", target = "email"),
            @Mapping(source = "roles", target = "roles")
    })
    public abstract Member toMember(MemberDto.MemberRequest memberRequest, Set<RoleCode> roles);

    public abstract MemberDto.MemberResponse toMemberResponse(Member member);

    @Named("encryptPassword")
    String encryptPassword(String password) {
        return bCryptPasswordEncoder.encode(password);
    }

}
