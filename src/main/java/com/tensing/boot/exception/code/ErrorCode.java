package com.tensing.boot.exception.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Security
    NOT_FOUND_MEMBER(400, "M001", "not found member data"),
    INVALID_JWT(400, "M002", "invalid jwt token"),

    // Common
    COMMON_ERROR(400, "C001", "Common error"),
    HANDLE_ACCESS_DENIED(403, "C002", "access denied"),
    INTERNAL_SERVER_ERROR(500, "C000", "Internal server error");


    private final int status;
    private final String code;
    private final String message;

}