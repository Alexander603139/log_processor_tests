package io.github.bugrov.log_processor_test.client;

import io.github.bugrov.log_processor_test.dto.LogRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogApiClient {

    private final RestTemplate restTemplate;

    @Value("${app.base-url}")
    private String baseUrl;

    public ResponseEntity<Void> sendEvent(LogRequest request) {
        String url = baseUrl + "/api/v1/events";
        log.debug("Sending event to {}", url);
        return restTemplate.postForEntity(url, request, Void.class);
    }
}