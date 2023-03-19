package com.tensing.boot.global.config.properties;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationPropertiesScan(basePackageClasses = PropertiesConfiguration.class)
public class PropertiesConfiguration {
    // basePackageClasses 기준으로 ConfigurationProperties 가 달린 클래스들을 빈으로 등록
    // Spring boot 2.6 버전 부터는 @ConstructorBinding 이 default 적용
    // Immutable 객체에 @ConfigurationProperties 사용할려면 @ConfigurationPropertiesScan 등을 이용하여 빈으로 등록해야함.
    // Immutable 객체는 @Component 등을 직접 달아서 빈등록 X
}
