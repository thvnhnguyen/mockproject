package com.thvnhng.mockproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.thvnhng.mockproject.Repository")
public class MockprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockprojectApplication.class, args);
    }

}
