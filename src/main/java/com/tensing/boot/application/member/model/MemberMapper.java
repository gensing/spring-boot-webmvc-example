package com.tensing.boot.application.member.model;

import com.tensing.boot.application.member.model.dto.MemberDto;
import com.tensing.boot.application.member.model.data.MemberEntity;
import com.tensing.boot.global.filters.security.model.code.RoleCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;


@Mapper(componentModel = "spring")
public abstract class MemberMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "memberRequest.username", target = "username"),
            @Mapping(source = "memberRequest.password", target = "password", qualifiedByName = "encryptPassword"),
            @Mapping(source = "memberRequest.email", target = "email"),
            @Mapping(source = "roles", target = "roles")
    })
    public abstract MemberEntity toMember(MemberDto.MemberRequest memberRequest, Set<RoleCode> roles);

    public abstract MemberDto.MemberResponse toMemberResponse(MemberEntity memberEntity);

    @Named("encryptPassword")
    String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
