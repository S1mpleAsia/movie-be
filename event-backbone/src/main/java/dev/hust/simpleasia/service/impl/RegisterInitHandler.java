package dev.hust.simpleasia.service.impl;

import dev.hust.simpleasia.core.utils.EventType;
import dev.hust.simpleasia.core.utils.TopicName;
import dev.hust.simpleasia.entity.dto.RegisterInitReq;
import dev.hust.simpleasia.port.KafkaClient;
import dev.hust.simpleasia.service.EventMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegisterInitHandler extends EventHandlerImpl<RegisterInitReq>{
    private final KafkaClient kafkaClient;

    protected RegisterInitHandler(EventMessagingService messagingService,
                                  KafkaClient kafkaClient) {
        super(messagingService);
        this.kafkaClient = kafkaClient;
    }

    @Override
    public EventType eventType() {
        return EventType.INIT_SIGNUP;
    }

    @Override
    protected void forward(RegisterInitReq payload) {
        kafkaClient.put(TopicName.AUTH_SERVICE.getName(), payload.getOrderId(), payload);
    }

    @Override
    protected Class<RegisterInitReq> clazz() {
        return RegisterInitReq.class;
    }
}
