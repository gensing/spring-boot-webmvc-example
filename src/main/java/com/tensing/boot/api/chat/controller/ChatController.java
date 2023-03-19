package com.tensing.boot.api.chat.controller;

import com.tensing.boot.api.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class ChatController {

    private final ChatService chatService;

    // 소켓 보안 추가 할 사항
    // 1. 스프링 시큐리티를 이용하여 임시 인증 api 생성 ( 해당 api 호출시 인증 확인 후 otp send to client and save in redis (5분짜리) )
    // 2. 소켓 구독시 otp 확인 후 연결.
    // 3. 실시간 벤 등 정보 기입, 구독 시 redis or 메모리에 user info(name, ip, 권한 등등) 저장 후 send 시마다 확인 후 분기 처리

    // ChatController 기능
    // 채팅 방 생성
    // 채팅 방 정보
    // 채팅 방 수정
    // 채팅 방 삭제

}
