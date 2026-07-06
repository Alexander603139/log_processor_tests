package io.github.bugrov.log_processor_test.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "event_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MongoLogDocument {
    private String id;
    private String source;
    private String ipAddress;
    private String userAgent;
    private String rawMessage;
    private Instant timestamp;
}
