package dev.hust.simpleasia.core.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse<T> {
    private ResponseStatus status;
    private T data;

    public static <T> GeneralResponse<T> success(T data) {
        return new GeneralResponse<>(
                ResponseStatus.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(HttpStatus.OK.name())
                        .timestamp(new Date())
                        .build(), data);
    }

    public static <T> GeneralResponse<T> error(String message, Throwable ex) {
        return new GeneralResponse<>(
                ResponseStatus.builder()
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(message)
                        .error(ex.getMessage())
                        .timestamp(new Date())
                        .build(), null);
    }

    public static <T> GeneralResponse<T> error(HttpStatus status, String message, Throwable ex) {
        return new GeneralResponse<>(
                ResponseStatus.builder()
                        .statusCode(status.value())
                        .message(message)
                        .error(ex.getMessage())
                        .timestamp(new Date())
                        .build(), null);
    }
}
