package org.findy.findy_be;

import org.findy.findy_be.auth.properties.CorsProperties;
import org.findy.findy_be.common.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({
	CorsProperties.class,
	AppProperties.class
})
@OpenAPIDefinition(
	servers = {
		@Server(url = "http://localhost:8080", description = "Default Server url")
	}
)
public class FindyBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindyBeApplication.class, args);
	}

}
