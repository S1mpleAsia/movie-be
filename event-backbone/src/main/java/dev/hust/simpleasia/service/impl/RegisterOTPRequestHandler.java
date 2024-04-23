package dev.hust.simpleasia.service.impl;

import dev.hust.simpleasia.core.utils.EventType;
import dev.hust.simpleasia.core.utils.TopicName;
import dev.hust.simpleasia.entity.dto.RegisterOTPRequest;
import dev.hust.simpleasia.port.KafkaClient;
import dev.hust.simpleasia.service.EventMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RegisterOTPRequestHandler extends EventHandlerImpl<RegisterOTPRequest>{
    private final KafkaClient kafkaClient;
    protected RegisterOTPRequestHandler(EventMessagingService messagingService,
                                        KafkaClient kafkaClient) {
        super(messagingService);
        this.kafkaClient = kafkaClient;
    }

    @Override
    public EventType eventType() {
        return EventType.INIT_OTP_SIGNUP;
    }

    @Override
    protected void forward(RegisterOTPRequest payload) {
        kafkaClient.put(TopicName.AUTH_SERVICE.getName(), payload.getOrderId(), payload);
    }

    @Override
    protected Class<RegisterOTPRequest> clazz() {
        return RegisterOTPRequest.class;
    }
}
