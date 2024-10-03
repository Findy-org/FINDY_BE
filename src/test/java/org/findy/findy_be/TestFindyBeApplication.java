package org.findy.findy_be;

import org.springframework.boot.SpringApplication;

public class TestFindyBeApplication {

	public static void main(String[] args) {
		SpringApplication.from(FindyBeApplication::main).with(TestContainerConfiguration.class).run(args);
	}
}