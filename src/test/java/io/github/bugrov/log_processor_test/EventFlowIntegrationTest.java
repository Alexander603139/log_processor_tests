package io.github.bugrov.log_processor_test;


import io.github.bugrov.log_processor_test.client.LogApiClient;
import io.github.bugrov.log_processor_test.dto.LogRequest;
import io.github.bugrov.log_processor_test.repository.MongoLogRepository;
import io.github.bugrov.log_processor_test.repository.PostgresLogRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("integration")
class EventFlowIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private LogApiClient logApiClient;

    @Autowired
    private PostgresLogRepository postgresRepo;

    @Autowired
    private MongoLogRepository mongoRepo;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void shouldProcessEventAndSaveToMongoDB() {
        // given
        String ip = "10.0.0.99";
        LogRequest request = new LogRequest("TEST", ip, "curl", "{\"msg\":\"hello\"}", null);

        // when
        ResponseEntity<Void> response = restTemplate.postForEntity(
                "/api/v1/events", request, Void.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        // wait for consumer to process
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .until(() -> {
                    Query query = new Query(Criteria.where("ipAddress").is(ip));
                    return mongoTemplate.exists(query, "event_logs");
                });
    }

    @Test
    void shouldProcessEventAndSaveToBothDatabases() {
        // given
        String ip = "10.0.0.99";
        LogRequest request = new LogRequest();
        request.setSource("TEST");
        request.setIpAddress(ip);
        request.setUserAgent("curl");
        request.setRawMessage("{\"msg\":\"hello\"}");
        request.setTimestamp(null);

        // when
        ResponseEntity<Void> response = logApiClient.sendEvent(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        // wait for async processing
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .until(() -> {
                    boolean inPostgres = postgresRepo.findById(ip) != null; // упрощённо
                    boolean inMongo = mongoRepo.existsById(ip);
                    return inPostgres && inMongo;
                });
    }
}
