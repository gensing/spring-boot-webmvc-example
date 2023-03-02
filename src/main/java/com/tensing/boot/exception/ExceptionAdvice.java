package com.tensing.boot.exception;

import com.tensing.boot.exception.code.ErrorCode;
import com.tensing.boot.exception.exception.BusinessException;
import com.tensing.boot.exception.payload.ErrorMessages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorMessages> handleBindException(BindException e) {
        log.info("handleBindException() ", e);
        final var errorCode = ErrorCode.BAD_REQUEST;
        final var messages = e.getBindingResult().getFieldErrors().stream().map(error -> getResultMessage(error)).toList();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorMessages.of(errorCode, messages));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorMessages> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("handleMethodArgumentNotValidException() ", e);
        final var errorCode = ErrorCode.BAD_REQUEST;
        final var messages = e.getBindingResult().getFieldErrors().stream().map(error -> getResultMessage(error)).toList();
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorMessages.of(errorCode, messages));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorMessages> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("handleMethodArgumentTypeMismatchException() ", e);
        final var errorCode = ErrorCode.BAD_REQUEST;
        final var message = getResultMessage(e.getName(), e.getValue(), e.getErrorCode());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorMessages.of(errorCode, message));
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
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

    /**
     * Authentication 객체가 필요한 권한을 보유하지 않은 경우 발생
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorMessages> handleAccessDeniedException(AccessDeniedException e) {
        log.info("handleAccessDeniedException() ", e);
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

    private String getResultMessage(FieldError fieldError) {
        final var field = fieldError.getField();
        final var rejectedValue = fieldError.getRejectedValue();
        final var defaultMessage = fieldError.getDefaultMessage();
        return getResultMessage(field, rejectedValue, defaultMessage);
    }

    private String getResultMessage(String field, Object rejectedValue, String defaultMessage) {
        return String.format("field: %s, value: %s, message: %s", field, rejectedValue, defaultMessage);
    }
}