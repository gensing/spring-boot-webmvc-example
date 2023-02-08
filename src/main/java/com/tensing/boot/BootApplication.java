package com.tensing.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


@SpringBootApplication
public class BootApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(BootApplication.class, args);
        DispatcherServlet dispatcherServlet = (DispatcherServlet) ctx.getBean("dispatcherServlet");
    }

    // 오른쪽 순으로 가장 최신 방식
    // CommandLineRunner -> ApplicationRunner -> @EventListener : 최초 구동시 실행 되여야 하는 로직이 필요하실 해당 방법 사용
}
