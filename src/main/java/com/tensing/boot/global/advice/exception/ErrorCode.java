package com.tensing.boot.global.advice.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // Common
    COMMON_ERROR(400, "C001", "Common error"),
    INTERNAL_SERVER_ERROR(500, "C000", "Internal server error");
    private final int status;
    private final String code;
    private final String message;

}