package dev.hust.simpleasia.service.event.impl;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import dev.hust.simpleasia.core.utils.EventType;
import dev.hust.simpleasia.core.utils.TopicName;
import dev.hust.simpleasia.entity.dto.RegisterInitDTO;
import dev.hust.simpleasia.entity.event.RegisterInitReq;
import dev.hust.simpleasia.entity.event.RegisterInitResp;
import dev.hust.simpleasia.port.KafkaClient;
import dev.hust.simpleasia.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterInitReqHandler extends BaseEventHandler<RegisterInitReq> {
    private final AuthService authService;
    private final KafkaClient kafkaClient;

    @Override
    protected Class<RegisterInitReq> clazz() {
        return RegisterInitReq.class;
    }

    @Override
    protected void process(RegisterInitReq payload) {
        GeneralResponse<RegisterInitResp> response = authService.initRegister(payload);

        kafkaClient.put(TopicName.EVENT_BACKBONE.getName(), response.getData().getOrderId(), response.getData());
    }

    @Override
    public EventType eventType() {
        return EventType.INIT_SIGNUP;
    }
}
