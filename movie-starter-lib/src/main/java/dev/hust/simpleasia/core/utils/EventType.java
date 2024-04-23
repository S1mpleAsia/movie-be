package dev.hust.simpleasia.core.utils;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum EventType {
    NOTIFICATION("NOTI"),
    MESSAGING("MSG"),
    INIT_SIGNUP("ISU"),
    INIT_SIGNUP_RESP("ISP"),
    INIT_OTP_SIGNUP("IOS"),
    PROCESS_SIGNUP("PSU"),
    VERIFY_SIGNUP("VSU"),
    FEEDBACK("FED"),
    SUBSCRIPTION("SUB"),
    ADD_FAVOURITE("FAV"),
    UPDATE_PROFILE("UPF");
    private final String type;

    EventType(String type) {
        this.type = type;
    }

    @JsonValue
    public String getType() {
        return type;
    }
}
