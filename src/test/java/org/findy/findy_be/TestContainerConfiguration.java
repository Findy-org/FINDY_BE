package org.findy.findy_be;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainerConfiguration {

	@Bean
	@ServiceConnection
	public PostgreSQLContainer<?> postgreSQLContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
	}
}