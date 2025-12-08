package com.example.msordenes;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@EnableJpaRepositories
@SpringBootApplication
public class MsOrdenesApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsOrdenesApplication.class, args);
    }
}
