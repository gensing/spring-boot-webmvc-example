package com.tensing.boot;

import com.tensing.boot.configuration.properties.TensingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.DispatcherServlet;


@Slf4j
@RequiredArgsConstructor
@EnableJpaAuditing // AuditingEntityListener 사용을 위한 설정
@SpringBootApplication
public class BootApplication implements ApplicationRunner {

    private final TensingProperties tensingProperties;

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(BootApplication.class, args);
        DispatcherServlet dispatcherServlet = (DispatcherServlet) ctx.getBean("dispatcherServlet");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.debug("======================");
        log.debug(tensingProperties.toString());
        log.debug("======================");
    }

}
