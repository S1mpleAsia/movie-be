package dev.hust.simpleasia.utils;

import lombok.Getter;

@Getter
public enum PackageType {
    BASIC(999L), STANDARD(1299L), PREMIUM(1499L);
    private final Long value;

    PackageType(Long value) {
        this.value = value;
    }
}
