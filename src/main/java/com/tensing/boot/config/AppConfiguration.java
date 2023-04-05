package com.tensing.boot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.tensing.boot.config.properties.JwtProperties;
import com.tensing.boot.common.modules.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Configuration
public class AppConfiguration {

    private final JwtProperties tensingProperties;

    @Bean
    public TokenProvider accessTokenProvider() {
        JwtProperties.TokenProperties jwt = tensingProperties.accessToken();
        TokenProvider tokenProvider = new TokenProvider(jwt.secretKey(), jwt.signatureAlgorithm(), jwt.expirationInMs());
        return tokenProvider;
    }

    @Bean
    public TokenProvider refreshTokenProvider() {
        JwtProperties.TokenProperties jwt = tensingProperties.refreshToken();
        TokenProvider tokenProvider = new TokenProvider(jwt.secretKey(), jwt.signatureAlgorithm(), jwt.expirationInMs());
        return tokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule());
        return objectMapper;
    }

    @Bean
    public JavaTimeModule javaTimeModule(){
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // formatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // deserializers
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        // serializers
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));

        return javaTimeModule;
    }

}
