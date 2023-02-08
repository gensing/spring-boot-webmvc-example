package com.tensing.boot.configuration.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


// boot 3버전 부터는 ConstructorBinding 이 default 적용되는 것 같다.


@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "tensing")
public final class TensingProperties {
    private final boolean enable;
    
    // 클래스를 넣을 때도 final 로
    // private final Api api;

}
