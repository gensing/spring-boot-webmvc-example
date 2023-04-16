package com.tensing.boot.config;

import com.tensing.boot.common.filter.mdc.MDCLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class FilterConfiguration {

    // 로그 트랜잭션 번호 생성 필터 등록
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public MDCLoggingFilter mdcLoggingFilter() {
        MDCLoggingFilter mdcLoggingFilter = new MDCLoggingFilter();
        return mdcLoggingFilter;
    }

    // 공용 요청 로그 필터 등록
    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true);
        filter.setIncludeHeaders(true);
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(1000);
        return filter;
    }

}