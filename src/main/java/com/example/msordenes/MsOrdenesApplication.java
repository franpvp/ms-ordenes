package com.example.msordenes;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class MsOrdenesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsOrdenesApplication.class, args);
    }
}
