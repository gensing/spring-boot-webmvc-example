package com.tensing.boot.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice
public class TestResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 이곳에서 에러가 발생시 익셉션 핸들러 위임 X -> error controller 설정 필요 <- 다시 에러 발생시 여부 체크해보자.. 안나는것 같기도?
        // 컨트롤러, 리턴타입, 어노테이션 등에 따른 다음 로직 실행 조건을 이곳에서 설정한다.
        return returnType.getContainingClass() == TestController.class && returnType.getNestedParameterType() == String.class;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        // "이곳에서 에러가 발생시 익셉션 핸들러로 위임됨"
        //if (returnType.getContainingClass() == SampleController.class) throw new RuntimeException();

        // 익셉션 핸들러를 리턴으로는, MethodParameter 통해서는 초기 컨트롤러를 알 수가 없다. ( 혹시 아는 사람? )
        // 익셉션 핸들러를 타고 온 녀석이 초기 컨트롤에 따라 이곳에서 분기 처리를 해야한다면? 초기 컨트롤러 정보를 어떻게 알 수 있나? request.getURI() or 쓰레드 로컬을 이용한 변수 저장

        return body + " ResponseBodyAdvice";
    }
}
