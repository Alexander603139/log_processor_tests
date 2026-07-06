package io.github.bugrov.log_processor_test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogRequest {
    private String source;        // Например: "PYTHON_APP", "NGINX"
    private String ipAddress;
    private String userAgent;
    private String rawMessage;    // JSON-блоб от источника
    private Long timestamp;       // Unix time

    public LogRequest() {

    }
}
