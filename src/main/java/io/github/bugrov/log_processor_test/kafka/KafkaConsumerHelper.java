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
            // Получаем партиции топика
            var partitions = consumer.partitionsFor(topic);
            if (partitions.isEmpty()) {
                log.warn("No partitions found for topic {}", topic);
                return null;
            }
            // Назначаем все партиции вручную (без subscribe)
            var topicPartitions = partitions.stream()
                    .map(p -> new org.apache.kafka.common.TopicPartition(topic, p.partition()))
                    .collect(java.util.stream.Collectors.toList());
            consumer.assign(topicPartitions);

            // Перемещаемся в конец очереди
            consumer.seekToEnd(topicPartitions);
            // Читаем последнее сообщение из каждой партиции – берём самое последнее
            ConsumerRecords<String, String> records = consumer.poll(timeout);
            if (records.isEmpty()) {
                log.debug("No messages in topic {}", topic);
                return null;
            }
            // Берём последнее сообщение (с самой большой позицией)
            var iterator = records.iterator();
            var lastRecord = iterator.next();
            while (iterator.hasNext()) {
                lastRecord = iterator.next();
            }
            return lastRecord.value();
        } catch (Exception e) {
            log.error("Error reading from Kafka topic {}", topic, e);
            return null;
        }
    }
}