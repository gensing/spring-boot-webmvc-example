package com.tensing.boot.global.exception;

import com.tensing.boot.global.exception.exception.BusinessException;
import com.tensing.boot.global.exception.model.code.ErrorCode;
import com.tensing.boot.global.exception.model.dto.ErrorMessages;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@Hidden
@RestController
@RestControllerAdvice
public class GlobalExceptionAdvice implements ErrorController {

    // https://velog.io/@gillog/Spring-404-Error-Custom%ED%95%98%EA%B8%B0-ExceptionHandler%EC%82%AC%EC%9A%A9-NoHandlerFoundException-Throw-%EC%95%88%EB%90%A0%EB%95%8C
    // https://tecoble.techcourse.co.kr/post/2021-11-24-spring-customize-unhandled-api/

    // HandlerExceptionResolver 확인해보기 - 현재는 에러 발생시 dispacther 에서 mdc가 소멸한다.

    // 이곳을 타면 웹어서 검은 배경이 적용되어 보인다 ( 코드 문제가 아닌 /error url을 어디서 캐취하는 듯 )... postman 테스트시 상관 없음.
    // "/error" 매핑이 호출 되는 경우들을 확인하기. 현재 확인: spring 영역이 아닌 곳에서 일어나는 에러는 이곳을 탄다.
    @GetMapping(value = "/error", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ErrorMessages> error(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        int statusCode = status == null ? HttpStatus.OK.value() : Integer.valueOf(status.toString());
        String message = switch (HttpStatus.valueOf(statusCode)) {
            case OK -> "ERROR";
            case NOT_FOUND -> "NOT FOUND";
            case INTERNAL_SERVER_ERROR -> "INTERNAL SERVER ERROR";
            default -> String.valueOf(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
        };

        return ResponseEntity
                .status(statusCode)
                .body(ErrorMessages.of(message));
    }

    /**
     * 사용자 정의 익셉션
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorMessages> handleBusinessException(BusinessException e) {
        log.info("handleBusinessException() ", e);
        return getCommonResponse(e.getErrorCode());
    }

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