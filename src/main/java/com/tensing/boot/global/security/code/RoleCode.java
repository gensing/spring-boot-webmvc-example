package com.tensing.boot.global.security.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum RoleCode {
    ADMIN(RoleCode.ADMIN_VALUE, "1"),
    USER(RoleCode.USER_VALUE, "2");

    private final String roleName;
    private final String desc;
    public static final String ADMIN_VALUE = "ROLE_ADMIN";
    public static final String USER_VALUE = "ROLE_USER";
    private static final Map<String, RoleCode> descMap = Stream.of(RoleCode.values()).collect(Collectors.toUnmodifiableMap(RoleCode::getDesc, Function.identity()));

    public static RoleCode getByDesc(String desc) {
        return descMap.get(desc);
    }

    public static RoleCode findByDesc(String desc) {
        final RoleCode roleCode = descMap.get(desc);
        if (roleCode == null) throw new RuntimeException();
        return roleCode;
    }
}
