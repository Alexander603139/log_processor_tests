package io.github.bugrov.log_processor_test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {
    // Все настройки берутся из application-test.yml
    // Никаких @DynamicPropertySource

    @BeforeAll
    static void setupTunnel() throws Exception {
        SshTunnelManager.startTunnel();
    }

    @AfterAll
    static void tearDownTunnel() {
        SshTunnelManager.stopTunnel();
    }
}