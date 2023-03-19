package com.tensing.boot.api.chat.service;

public interface ChatService {
    public void convertAndSendToMessageBroker(String destination, String payload);

    public void convertAndSendToClient(String destination, String payload);
}
