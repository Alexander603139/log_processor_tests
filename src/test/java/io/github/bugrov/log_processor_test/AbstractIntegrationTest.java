package io.github.bugrov.log_processor_test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    private static boolean useRealServices() {
        return System.getenv("SPRING_DATASOURCE_URL") != null &&
                System.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS") != null;
    }

    @Container
    protected static final PostgreSQLContainer<?> postgres = useRealServices() ? null :
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("logdb")
                    .withUsername("admin")
                    .withPassword("admin");

    @Container
    protected static final MongoDBContainer mongo = useRealServices() ? null :
            new MongoDBContainer("mongo:7-jammy")
                    .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
                    .withEnv("MONGO_INITDB_ROOT_PASSWORD", "admin")
                    .withEnv("MONGO_INITDB_DATABASE", "logdb");

    @Container
    protected static final KafkaContainer kafka = useRealServices() ? null :
            new KafkaContainer(DockerImageName.parse("bitnamilegacy/kafka:4.0.0-debian-12-r10")
                    .asCompatibleSubstituteFor("confluentinc/cp-kafka"));

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        if (useRealServices()) {
            registry.add("spring.datasource.url", () -> System.getenv("SPRING_DATASOURCE_URL"));
            registry.add("spring.datasource.username", () -> System.getenv("SPRING_DATASOURCE_USERNAME"));
            registry.add("spring.datasource.password", () -> System.getenv("SPRING_DATASOURCE_PASSWORD"));
            registry.add("spring.data.mongodb.uri", () -> System.getenv("SPRING_DATA_MONGODB_URI"));
            registry.add("spring.kafka.bootstrap-servers", () -> System.getenv("SPRING_KAFKA_BOOTSTRAP_SERVERS"));
        } else {
            registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
            registry.add("spring.datasource.username", () -> postgres.getUsername());
            registry.add("spring.datasource.password", () -> postgres.getPassword());
            registry.add("spring.data.mongodb.uri", () ->
                    String.format("mongodb://admin:admin@localhost:%d/logdb?authSource=admin", mongo.getMappedPort(27017)));
            registry.add("spring.kafka.bootstrap-servers", () -> kafka.getBootstrapServers());
        }
        // app.base-url не добавляем – используем @LocalServerPort в тестах
    }
}