package com.tensing.boot.api.chat;

import com.tensing.boot.security.code.RoleCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/room/{roomId}") // pub
    public void sendMessage(@DestinationVariable String roomId, @Payload String chatMessage, StompHeaderAccessor stompHeaderAccessor) {
        messagingTemplate.convertAndSend("/topic/room/" + roomId, chatMessage + stompHeaderAccessor.getUser().toString());
    }

    // SimpleBrokerMessageHandler
    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        log.info("handleException");
        return exception.getMessage();
    }

}
