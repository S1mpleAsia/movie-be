package dev.hust.simpleasia.core.service;

import dev.hust.simpleasia.core.utils.JsonUtils;
import dev.hust.simpleasia.port.KafkaClient;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaClientAdapter implements KafkaClient {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public KafkaClientAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void put(String topic, String key, Object value) {
        kafkaTemplate.send(topic, key, JsonUtils.mapToString(value));
    }
}
