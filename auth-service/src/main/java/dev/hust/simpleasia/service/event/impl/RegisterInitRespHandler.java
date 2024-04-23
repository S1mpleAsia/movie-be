package dev.hust.simpleasia.service.event.impl;

import dev.hust.simpleasia.core.utils.EventType;
import dev.hust.simpleasia.core.utils.TopicName;
import dev.hust.simpleasia.entity.domain.CustomerOTP;
import dev.hust.simpleasia.entity.event.RegisterInitResp;
import dev.hust.simpleasia.entity.event.RegisterOTPRequest;
import dev.hust.simpleasia.port.KafkaClient;
import dev.hust.simpleasia.service.CustomerOTPService;
import dev.hust.simpleasia.utils.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegisterInitRespHandler extends BaseEventHandler<RegisterInitResp> {
    private final CustomerOTPService otpService;
    private final KafkaClient kafkaClient;

    @Override
    public EventType eventType() {
        return EventType.INIT_SIGNUP_RESP;
    }

    @Override
    protected Class<RegisterInitResp> clazz() {
        return RegisterInitResp.class;
    }

    @Override
    protected void process(RegisterInitResp payload) {
        CustomerOTP customerOTP = CustomerOTP.builder()
                .orderId(payload.getOrderId())
                .email(payload.getEmail())
                .build();

        otpService.generateOTP(customerOTP);
        RegisterOTPRequest registerOTPRequest = RegisterOTPRequest.builder()
                .orderId(payload.getOrderId())
                .email(payload.getEmail())
                .status(AccountStatus.PENDING.getStatus())
                .eventType(EventType.INIT_OTP_SIGNUP)
                .build();

        kafkaClient.put(TopicName.EVENT_BACKBONE.getName(), payload.getOrderId(), registerOTPRequest);
    }
}
