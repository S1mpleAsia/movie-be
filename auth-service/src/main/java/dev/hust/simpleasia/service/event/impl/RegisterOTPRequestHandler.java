package dev.hust.simpleasia.service.event.impl;

import dev.hust.simpleasia.core.utils.EventType;
import dev.hust.simpleasia.entity.event.RegisterOTPRequest;
import dev.hust.simpleasia.service.AuthService;
import dev.hust.simpleasia.utils.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterOTPRequestHandler extends BaseEventHandler<RegisterOTPRequest> {
    private final AuthService authService;

    @Override
    public EventType eventType() {
        return EventType.INIT_OTP_SIGNUP;
    }

    @Override
    protected Class<RegisterOTPRequest> clazz() {
        return RegisterOTPRequest.class;
    }

    @Override
    protected void process(RegisterOTPRequest payload) {
        authService.updateStatus(payload.getEmail(), AccountStatus.PENDING.getStatus());
    }
}
