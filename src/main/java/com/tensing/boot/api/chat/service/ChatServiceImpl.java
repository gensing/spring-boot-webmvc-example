package com.tensing.boot.api.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatServiceImpl implements ChatService {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void convertAndSendToMessageBroker(String destination, String payload) {
        kafkaTemplate.send(destination, payload);
    }

    @Override
    public void convertAndSendToClient(String destination, String payload) {
        messagingTemplate.convertAndSend(destination, payload);
    }
}
