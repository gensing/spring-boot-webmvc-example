package com.tensing.boot.common.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;


// https://mangkyu.tistory.com/266
public class MDCLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
        // web server 트랜잭션 id 가져오기
        String requestId = ((HttpServletRequest) servletRequest).getHeader("X-RequestID");
        MDC.put("request_id", requestId == null || requestId.trim() == "" ? UUID.randomUUID().toString() : requestId);
        filterChain.doFilter(servletRequest, servletResponse);
        MDC.clear();
    }

    @Override
    public void destroy() {
    }

}