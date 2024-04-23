package dev.hust.simpleasia.core.utils;

import lombok.Getter;

@Getter
public enum TopicName {
    EVENT_BACKBONE("event-backbone"),
    AUTH_SERVICE("auth-service");
    private final String name;

    TopicName(String name) {
        this.name = name;
    }

}
