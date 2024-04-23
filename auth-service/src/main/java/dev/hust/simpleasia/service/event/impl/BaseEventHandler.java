package dev.hust.simpleasia.service.event.impl;

import dev.hust.simpleasia.core.exception.BusinessException;
import dev.hust.simpleasia.core.utils.JsonUtils;
import dev.hust.simpleasia.entity.event.BaseEvent;
import dev.hust.simpleasia.service.event.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

@Slf4j
public abstract class BaseEventHandler<T extends BaseEvent> implements EventHandler {
    @Override
    public void handle(String payload) {
        try {
            String correlationId = UUID.randomUUID().toString();
            ThreadContext.put("correlationId", correlationId);
            log.info("EventHandler.handle event: [{}]", payload);
            T concretePayload = JsonUtils.mapToJson(payload, clazz());
            process(concretePayload);
            log.info("EventHandler.handle success");
        } catch (BusinessException ex) {
            log.error("Business exception: [{}]", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            log.error("Unknown exception", ex);
            throw new BusinessException(ex.getMessage());
        }
    }

    protected abstract Class<T> clazz();

    protected abstract void process(T payload);
}
