package com.tensing.boot.application.chat.socket;

import com.tensing.boot.application.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatSocket {

    private final ChatService chatService;
    private final String BROKER_CHAT_TOPIC = "chat-topic";


    @MessageMapping("/chat") // pub
    public void sendMessage(@Headers MessageHeaders headers, @Payload String chatMessage) {
        chatService.convertAndSendToMessageBroker(BROKER_CHAT_TOPIC, chatMessage);
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        log.info("handleException");
        log.info(exception.getMessage());
        return exception.getMessage();
    }

    @KafkaListener(topics = BROKER_CHAT_TOPIC, id = "chat-listener")
    public void receiveMessage(@Headers MessageHeaders headers, @Payload String payload) {
        chatService.convertAndSendToClient("/topic/chat", payload);
    }

}
