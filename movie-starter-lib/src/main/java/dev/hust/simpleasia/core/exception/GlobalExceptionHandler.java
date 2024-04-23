package dev.hust.simpleasia.core.exception;

import dev.hust.simpleasia.core.entity.GeneralResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public GeneralResponse<String> error(BusinessException ex) {
        if (ex.getStatus() != null) return GeneralResponse.error(ex.getStatus(), ex.getMessage(), ex);
        else return GeneralResponse.error(ex.getMessage(), ex);
    }
}
