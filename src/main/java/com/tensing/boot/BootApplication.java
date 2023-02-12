package com.tensing.boot;

import com.tensing.boot.configuration.properties.TensingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class BootApplication implements ApplicationRunner {

    private final TensingProperties tensingProperties;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(BootApplication.class, args);
        DispatcherServlet dispatcherServlet = (DispatcherServlet) ctx.getBean("dispatcherServlet");
    }

    // CommandLineRunner -> ApplicationRunner -> @EventListener (오른쪽 순으로 가장 최신 방식)
    // 최초 구동시 실행 되여야 하는 로직이 필요하실 해당 방법 사용
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("======================");
        log.debug(tensingProperties.toString());
        log.debug("======================");
    }

}
