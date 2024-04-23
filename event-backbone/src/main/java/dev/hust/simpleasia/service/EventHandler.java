package dev.hust.simpleasia.service;


import dev.hust.simpleasia.core.utils.EventType;

public interface EventHandler {
    void handle(String payload);
    EventType eventType();
}
