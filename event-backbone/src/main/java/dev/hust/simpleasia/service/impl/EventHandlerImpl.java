package dev.hust.simpleasia.service.impl;

import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.core.utils.JsonUtils;
import dev.hust.simpleasia.entity.domain.EventMessaging;
import dev.hust.simpleasia.entity.dto.BaseEvent;
import dev.hust.simpleasia.service.EventHandler;
import dev.hust.simpleasia.service.EventMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.scheduling.annotation.Async;

import java.util.UUID;

@Slf4j
public abstract class EventHandlerImpl<T extends BaseEvent> implements EventHandler {
    private final EventMessagingService messagingService;

    protected EventHandlerImpl(EventMessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Async("threadPoolTaskExecutor")
    public void handle(String payload) {
        try {
            String correlationId = UUID.randomUUID().toString();
            ThreadContext.put("correlationId", correlationId);
            log.info("EventHandler.handle event: [{}]", payload);
            T concretePayload = JsonUtils.mapToJson(payload, clazz());
            process(concretePayload);
            log.info("EventHandler.handle success");
        } catch (Exception ex) {
            log.error("Unknown exception", ex);
            throw new BusinessException(ex.getMessage());
        }
    }

     protected void process(T payload) {
        store(payload);
        forward(payload);
    }

    protected void store(T payload) {
        EventMessaging eventMessaging = EventMessaging.builder()
                .orderId(payload.getOrderId())
                .eventType(payload.getEventType().getType())
                .requestBody(payload.toString())
                .build();

        messagingService.save(eventMessaging);
    }

    protected abstract void forward(T payload);

    protected abstract Class<T> clazz();
}
