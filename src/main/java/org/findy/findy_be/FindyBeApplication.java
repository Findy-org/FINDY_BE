package org.findy.findy_be;

import org.findy.findy_be.auth.entity.CorsProperties;
import org.findy.findy_be.common.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties({
	CorsProperties.class,
	AppProperties.class
})
public class FindyBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindyBeApplication.class, args);
	}

}
