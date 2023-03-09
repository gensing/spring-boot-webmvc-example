package com.tensing.boot.api.chat;

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
@RequestMapping("/api")
@RestController
public class ChatController {

    private final ChatService chatService;
    private final String BROKER_CHAT_TOPIC = "chat-topic";


    @MessageMapping("/chat") // pub
    public void sendMessage(@Headers MessageHeaders headers, @Payload String chatMessage) {
        chatService.convertAndSendToMessageBroker(BROKER_CHAT_TOPIC, chatMessage);
    }

    @KafkaListener(topics = BROKER_CHAT_TOPIC, id = "chat-listener")
    public void receiveMessage(@Headers MessageHeaders headers, @Payload String payload) {
        chatService.convertAndSendToClient("/topic/chat", payload);
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        log.info("handleException");
        log.info(ExceptionUtils.getStackTrace(exception));
        return exception.getMessage();
    }

}
