package io.github.bugrov.log_processor_test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class LogProcessorTestApplicationTests {

	@Test
	void contextLoads() {
	}

}
