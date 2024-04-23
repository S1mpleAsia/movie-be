package dev.hust.simpleasia.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import dev.hust.simpleasia.core.utils.EventType;
import dev.hust.simpleasia.core.utils.JsonUtils;
import dev.hust.simpleasia.entity.dto.BaseEvent;
import dev.hust.simpleasia.service.EventConnector;
import dev.hust.simpleasia.service.EventHandler;
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

    @KafkaListener(topics = "${app.event-backbone.kafka.topic}", groupId = "${app.kafka.groupId}")
    public void handle(@Payload String payload , @Headers Map<String, Object> headers) {
        log.info("Retrieved Kafka message: [{}] - headers: [{}]", payload, headers);

        BaseEvent baseEvent = JsonUtils.mapToJson(payload, BaseEvent.class);
        EventHandler eventHandler = eventConnector.get(baseEvent.getEventType());
        eventHandler.handle(payload);
    }
}
