package dev.hust.simpleasia.utils;

import lombok.Getter;

@Getter
public enum AccountStatus {
    ACTIVE("ACT"),
    INACTIVE("INA"),
    PENDING("PEN"),
    INIT("INIT");

    private final String status;

    AccountStatus(String status) {
        this.status = status;
    }
}
