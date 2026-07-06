package io.github.bugrov.log_processor_test.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "event_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostgresLogEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String source;

    @Column(name = "ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(columnDefinition = "TEXT")
    private String rawMessage;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
    // можно добавить поле severity или is_suspicious для быстрых выборок
}
