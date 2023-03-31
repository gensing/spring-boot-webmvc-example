package com.tensing.boot.global.advice.exception.model.code;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Security
    NOT_FOUND_MEMBER(400, "S001", "not found member data"),
    INVALID_JWT(400, "S002", "invalid jwt token"),

    NOT_BEARER_TOKEN(400, "S003", "not bearer token"),

    // member
    DUPLICATION_USER(400, "M001", "exists user name"),

    // Common
    COMMON_ERROR(400, "C001", "Common error"),
    HANDLE_ACCESS_DENIED(403, "C002", "access denied"),
    BAD_REQUEST(400, "C003", "bad request"),
    NOT_FOUND_DATA(400, "C004", "not found data"),
    INTERNAL_SERVER_ERROR(500, "C000", "Internal server error");


    private final int status;
    private final String code;
    private final String message;

}