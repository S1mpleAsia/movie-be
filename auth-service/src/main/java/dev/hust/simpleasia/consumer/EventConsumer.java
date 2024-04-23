package dev.hust.simpleasia.consumer;


import dev.hust.simpleasia.core.utils.JsonUtils;
import dev.hust.simpleasia.entity.event.BaseEvent;
import dev.hust.simpleasia.service.event.EventConnector;
import dev.hust.simpleasia.service.event.EventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventConsumer {
    private final EventConnector eventConnector;

    @KafkaListener(topics = "${app.auth-service.kafka.topic}", groupId = "${app.kafka.groupId}")
    public void handle(@Payload String payload , @Headers Map<String, Object> headers) {
        log.info("Retrieved Kafka message: [{}] - headers: [{}]", payload, headers);

        BaseEvent baseEvent = JsonUtils.mapToJson(payload, BaseEvent.class);
        EventHandler eventHandler = eventConnector.get(baseEvent.getEventType());
        eventHandler.handle(payload);
    }
}
