package com.tensing.boot.config;

import com.tensing.boot.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class StompConfiguration implements WebSocketMessageBrokerConfigurer, ChannelInterceptor {

    // STOMP : Simple Text Oriented Messaging Protocol

    // HandshakeInterceptor 연결 때 한 번 수행 (  )
    // ChannelInterceptor 연결 빼고 계속 수행, frame 접근 가능 ( jwt 를 이곳에서 가져올 수 있음 )

    private final SecurityService securityService;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")  // 소켓 연결시 사용
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // 구독시 사용
        registry.setApplicationDestinationPrefixes("/pub"); // pub 시 사용
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.setMessageSizeLimit(160 * 64 * 1024);
        registration.setSendTimeLimit(20 * 10000);
        registration.setSendBufferSizeLimit(10 * 512 * 1024);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(this);
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        log.info("preSend");

        final var accessor = StompHeaderAccessor.wrap(message);
        // var nowDestination = accessor.getNativeHeader("destination").get(0); // 구독 정보

        switch (accessor.getCommand()) {
            case CONNECT -> {
                final var bearerToken = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
                final var accessToken = (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) ? bearerToken.substring(7) : null;
                final var authentication = securityService.getAuthentication(accessToken);
            }
        }

        return message;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("postSend");
    }

}
