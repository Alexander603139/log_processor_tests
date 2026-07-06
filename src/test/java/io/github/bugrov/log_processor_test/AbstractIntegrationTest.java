package io.github.bugrov.log_processor_test;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
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
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    @Container
    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("logdb")
            .withUsername("admin")
            .withPassword("admin");

    @Container
    protected static final MongoDBContainer mongo = new MongoDBContainer("mongo:7-jammy")
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "admin")
            .withEnv("MONGO_INITDB_DATABASE", "logdb");

    @Container
    protected static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("bitnamilegacy/kafka:4.0.0-debian-12-r10")
                    .asCompatibleSubstituteFor("confluentinc/cp-kafka")
    );

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("POSTGRES_PORT", () -> postgres.getMappedPort(5432));
        registry.add("MONGO_PORT", () -> mongo.getMappedPort(27017));
        registry.add("KAFKA_PORT", () -> kafka.getMappedPort(9092));
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl());
        registry.add("spring.data.mongodb.uri", () ->
                String.format("mongodb://admin:admin@localhost:%d/logdb?authSource=admin", mongo.getMappedPort(27017)));
        registry.add("spring.kafka.bootstrap-servers", () -> kafka.getBootstrapServers());
    }
}