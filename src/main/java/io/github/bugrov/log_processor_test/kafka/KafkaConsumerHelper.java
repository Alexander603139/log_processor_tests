package io.github.bugrov.log_processor_test.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaConsumerHelper {

    private final ConsumerFactory<String, String> consumerFactory;

    public String consumeLastMessage(String topic, Duration timeout) {
        try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
            consumer.subscribe(Collections.singletonList(topic));
            consumer.poll(Duration.ofMillis(100)); // инициализация
            // Ищем последнее сообщение
            var partitions = consumer.partitionsFor(topic);
            if (partitions.isEmpty()) {
                log.warn("No partitions found for topic {}", topic);
                return null;
            }
            consumer.assign(partitions.stream()
                    .map(p -> new org.apache.kafka.common.TopicPartition(topic, p.partition()))
                    .collect(java.util.stream.Collectors.toList()));
            consumer.seekToEnd(consumer.assignment());
            for (var tp : consumer.assignment()) {
                long position = consumer.position(tp);
                if (position > 0) {
                    consumer.seek(tp, position - 1);
                } else {
                    consumer.seek(tp, 0);
                }
            }
            ConsumerRecords<String, String> records = consumer.poll(timeout);
            if (records.isEmpty()) {
                log.debug("No messages in topic {}", topic);
                return null;
            }
            var record = records.iterator().next();
            return record.value();
        } catch (Exception e) {
            log.error("Error reading from Kafka topic {}", topic, e);
            return null;
        }
    }
}