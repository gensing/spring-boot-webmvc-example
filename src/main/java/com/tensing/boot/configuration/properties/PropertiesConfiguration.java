package com.tensing.boot.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationPropertiesScan(basePackageClasses = PropertiesConfiguration.class)
public class PropertiesConfiguration {
    // basePackageClasses 기준으로 ConfigurationProperties 가 달린 클래스들을 빈으로 등록
    // boot 3버전 부터는 ConstructorBinding 이 default 적용되는 것 같다.
}
