package com.example.msordenes.application.kafka.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic pagoPendienteTopic(
            @Value("${app.kafka.topic.pago-pendiente:pago-pendiente}") String topic
    ) {
        return TopicBuilder
                .name(topic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
