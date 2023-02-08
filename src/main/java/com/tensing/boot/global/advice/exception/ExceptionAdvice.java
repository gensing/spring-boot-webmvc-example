package com.tensing.boot.global.advice.exception;

import com.tensing.boot.global.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<CommonResponse> handleBusinessException(BusinessException e) {
        log.info("handleBusinessException() ", e);
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(CommonResponse
                        .builder()
                        .status(errorCode.getStatus())
                        .body(ErrorMessages.of(errorCode))
                        .build()
                );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<CommonResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("handleHttpRequestMethodNotSupportedException() ", e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(CommonResponse
                        .builder()
                        .status(errorCode.getStatus())
                        .body(ErrorMessages.of(errorCode))
                        .build()
                );
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<CommonResponse> NoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.info("NoHandlerFoundExceptionException() ", e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(CommonResponse
                        .builder()
                        .status(errorCode.getStatus())
                        .body(ErrorMessages.of(errorCode))
                        .build()
                );
    }

    /**
     * 예상하지 못한 에러
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponse> handleException(Exception e) {
        log.error("handleException() ", e);
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(CommonResponse
                        .builder()
                        .status(errorCode.getStatus())
                        .body(ErrorMessages.of(errorCode))
                        .build()
                );
    }
}