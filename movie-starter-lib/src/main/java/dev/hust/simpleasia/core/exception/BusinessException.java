package dev.hust.simpleasia.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
    private HttpStatus status;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    public BusinessException(String message, HttpStatus status) {
        this(message);
        this.status = status;
    }

    public BusinessException(String message, HttpStatus status, Throwable ex) {
        this(message, ex);
        this.status = status;
    }

}
