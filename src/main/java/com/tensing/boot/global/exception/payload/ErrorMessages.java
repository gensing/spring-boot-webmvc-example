package com.tensing.boot.global.exception.payload;

import com.tensing.boot.global.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorMessages {
    private String code;
    private List<String> messages;

    private ErrorMessages(final ErrorCode errorCode) {
        this.code = errorCode.getCode();
        List<String> messages = new ArrayList<>();
        messages.add(errorCode.getMessage());
        this.messages = messages;
    }

    private ErrorMessages(final String messages) {
        this.code = "CUSTOM";
        List<String> messagesList = new ArrayList<>();
        messagesList.add(messages);
        this.messages = messagesList;
    }

    private ErrorMessages(final ErrorCode errorCode, final List<String> messages) {
        this.code = errorCode.getCode();
        this.messages = messages;
    }

    public static ErrorMessages of(final ErrorCode errorCode) {
        return new ErrorMessages(errorCode);
    }

    public static ErrorMessages of(final ErrorCode errorCode, final List<String> messages) {
        return new ErrorMessages(errorCode, messages);
    }

    public static ErrorMessages of(final ErrorCode errorCode, final String messages) {
        List<String> messagesList = new ArrayList<>();
        messagesList.add(messages);
        return new ErrorMessages(errorCode, messagesList);
    }

    public static ErrorMessages of(final String messages) {
        return new ErrorMessages(messages);
    }

}
