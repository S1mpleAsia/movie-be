package dev.hust.simpleasia.service.impl;

import dev.hust.simpleasia.core.utils.EventType;
import dev.hust.simpleasia.core.utils.TopicName;
import dev.hust.simpleasia.entity.dto.RegisterInitResp;
import dev.hust.simpleasia.port.KafkaClient;
import dev.hust.simpleasia.service.EventMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegisterInitRespHandler extends EventHandlerImpl<RegisterInitResp> {
    private final KafkaClient kafkaClient;
    protected RegisterInitRespHandler(EventMessagingService messagingService,
                                      KafkaClient kafkaClient) {
        super(messagingService);
        this.kafkaClient = kafkaClient;
    }

    @Override
    public EventType eventType() {
        return EventType.INIT_SIGNUP_RESP;
    }

    @Override
    protected void forward(RegisterInitResp payload) {
        kafkaClient.put(TopicName.AUTH_SERVICE.getName(), payload.getOrderId(), payload);
    }

    @Override
    protected Class<RegisterInitResp> clazz() {
        return RegisterInitResp.class;
    }
}
