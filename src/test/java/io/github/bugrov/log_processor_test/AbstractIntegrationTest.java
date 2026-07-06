package io.github.bugrov.log_processor_test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> getEnv("SPRING_DATASOURCE_URL"));
        registry.add("spring.datasource.username", () -> getEnv("SPRING_DATASOURCE_USERNAME"));
        registry.add("spring.datasource.password", () -> getEnv("SPRING_DATASOURCE_PASSWORD"));
        registry.add("spring.data.mongodb.uri", () -> getEnv("SPRING_DATA_MONGODB_URI"));
        registry.add("spring.kafka.bootstrap-servers", () -> getEnv("SPRING_KAFKA_BOOTSTRAP_SERVERS"));
        registry.add("app.base-url", () -> getEnv("APP_BASE_URL", "http://localhost:8081"));
    }

    private static String getEnv(String key) {
        return System.getenv(key);
    }

    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return value != null ? value : defaultValue;
    }
}