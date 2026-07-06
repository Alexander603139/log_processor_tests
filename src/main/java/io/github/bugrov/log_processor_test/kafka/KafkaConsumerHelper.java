package io.github.bugrov.log_processor_test.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerHelper {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public String consumeLastMessage(String topic, Duration timeout) {
        // Пока возвращаем null, реализуем позже
        log.warn("KafkaConsumerHelper not fully implemented");
        return null;
    }
}