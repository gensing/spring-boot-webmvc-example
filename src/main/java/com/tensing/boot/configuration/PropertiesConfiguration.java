package com.tensing.boot.configuration;

import com.tensing.boot.configuration.properties.TensingProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
//@ConfigurationPropertiesScan("com.tensing.boot.configuration.properties")
@EnableConfigurationProperties(value = {TensingProperties.class})
public class PropertiesConfiguration {
    // EnableConfigurationProperties 에 등록된 클래스들을 빈으로 등록
}
