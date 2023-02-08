package com.tensing.boot.configuration;

import com.tensing.boot.global.filter.MDCLoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class FilterConfiguration {

    // 로그 인코딩 설정
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> encodingFilterBean() {
        FilterRegistrationBean<CharacterEncodingFilter> registrationBean = new FilterRegistrationBean<CharacterEncodingFilter>();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setForceEncoding(true); // request, response encoding을 같게 설정한다.
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }

    // 로그 트랜잭션 번호 생성 필터 등록
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public MDCLoggingFilter mdcLoggingFilter() {
        return new MDCLoggingFilter();
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