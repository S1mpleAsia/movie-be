package dev.hust.simpleasia.utils;

public class Constants {
    public static final long OTP_VALID_DURATION = 30 * 1000; // Expired in 30s
    public static final String EXPIRED_MESSAGE = "Too long to response. The OTP has been deleted.";
    public static final String SUCCESS_MESSAGE = "Verify successfully, OTP is matched.";
    public static final String FAILED_MESSAGE = "Verify failed, OTP is not matched.";
}
