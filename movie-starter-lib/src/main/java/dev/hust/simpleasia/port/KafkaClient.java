package dev.hust.simpleasia.port;

public interface KafkaClient {
    void put(String topic, String key, Object value);
}
