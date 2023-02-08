package com.tensing.boot.global;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommonResponse<T> {
    private int status = 200;
    private T body;


    public CommonResponse(final T body) {
        this.body = body;
    }

    public CommonResponse(final int status, final T body) {
        this.status = status;
        this.body = body;
    }

}
