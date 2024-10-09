package org.findy.findy_be;

import org.findy.findy_be.common.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import({TestContainerConfiguration.class, SecurityConfig.class})
@SpringBootTest
@ActiveProfiles("test")
class FindyBeApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testDatabaseConnection() {
	}
}