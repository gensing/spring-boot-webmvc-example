package com.tensing.boot.global.advice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorMessages> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("handleHttpRequestMethodNotSupportedException() ", e);
        return getCommonResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorMessages> NoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.info("NoHandlerFoundExceptionException() ", e);
        return getCommonResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorMessages> handleAccessDeniedException(AccessDeniedException e) {
        log.error("handleAccessDeniedException() ", e);
        return getCommonResponse(ErrorCode.HANDLE_ACCESS_DENIED);
    }

    /**
     * 사용자 정의 익셉션
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorMessages> handleBusinessException(BusinessException e) {
        log.info("handleBusinessException() ", e);
        return getCommonResponse(e.getErrorCode());
    }

    /**
     * 예상하지 못한 에러
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorMessages> handleException(Exception e) {
        log.error("handleException() ", e);
        return getCommonResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity getCommonResponse(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorMessages.of(errorCode));
    }
}