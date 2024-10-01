package org.findy.findy_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FindyBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FindyBeApplication.class, args);
	}

}
