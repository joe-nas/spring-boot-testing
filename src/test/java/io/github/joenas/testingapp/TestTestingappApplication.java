package io.github.joenas.testingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestTestingappApplication {

	public static void main(String[] args) {
		SpringApplication.from(TestingappApplication::main).with(TestTestingappApplication.class).run(args);
	}

}
