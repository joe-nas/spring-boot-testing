package io.github.joenas.testingapp.integration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class AbstractContainerBaseTest {

    @Container
    static final MySQLContainer mysqlContainer;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    static {
        mysqlContainer = new MySQLContainer("mysql:latest")
                .withDatabaseName("test")
                .withUsername("test")
                .withPassword("test");
        ;
        mysqlContainer.start();
    }

}
