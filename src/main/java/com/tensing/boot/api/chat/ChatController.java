package com.tensing.boot.api.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/room/{roomId}") // pub
    @SendTo("/topic/room/{roomId}") // sub
    public String sendMessage(@DestinationVariable String roomId, @Payload String chatMessage) {
        //messagingTemplate.convertAndSend("/topic/room/" + roomId,chatMessage +" ttt");
        return chatMessage;
    }

    @MessageExceptionHandler
    public String handleException(Throwable exception) {
        log.info("handleException");
        return exception.getMessage();
    }

}
