package dev.hust.simpleasia.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hust.simpleasia.core.exception.BusinessException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonUtils {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static <T> T mapToJson(String payload, Class<T> responseType) {
        try {
            return objectMapper.readValue(payload, responseType);
        } catch (Exception ex) {
            throw new BusinessException("Can not parse payload");
        }
    }

    public static <T> T mapToJson(String payload, TypeReference<T> responseType) {
        try {
            return objectMapper.readValue(payload, responseType);
        } catch (Exception ex) {
            throw new BusinessException("Can not parse payload");
        }
    }

    public static String mapToString(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (Exception ex) {
            throw new BusinessException("Can not parse to string");
        }
    }
}
