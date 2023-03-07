package com.tensing.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class StompConfiguration implements WebSocketMessageBrokerConfigurer, ChannelInterceptor, HandshakeInterceptor {

    // STOMP : Simple Text Oriented Messaging Protocol
    
    // HandshakeInterceptor 연결 때 한 번 수행 (  )
    // ChannelInterceptor 연결 빼고 계속 수행

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp")  // 소켓 연결시 사용
                .setAllowedOriginPatterns("*")
                .addInterceptors(this)
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
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("beforeHandshake");
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("afterHandshake");
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("preSend");
        return message;
    }
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        log.info("postSend");
    }

    @EventListener
    public void onConnect(SessionConnectEvent event) {
        log.info("onConnect");
    }

    @EventListener
    public void onSubscribe(SessionSubscribeEvent event) {
        log.info("onSubscribe");
        // 이곳에서 권한 인증 추가
        // var accessor = StompHeaderAccessor.wrap(event.getMessage());
        //var nowDestination = accessor.getNativeHeader("destination").get(0);
        // if (!nowDestination.startsWith(destination)) return;
        //messagingTemplate.convertAndSend(nowDestination, "subscribe " + nowDestination);
    }

    @EventListener
    public void onUnsubscribe(SessionUnsubscribeEvent event) {
        log.info("onUnsubscribe");
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        log.info("onDisconnect");
    }
}
