package com.example.msordenes.application.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            DefaultErrorHandler errorHandler
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory);

        // ACK manual (importante para control de estado de orden)
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        // Retry + DLT handler
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> template) {

        // 3 reintentos, 1 segundo entre intentos
        var backoff = new FixedBackOff(1000L, 3L);

        var recoverer = new DeadLetterPublishingRecoverer(
                template,
                (record, ex) -> {
                    String dlt = record.topic() + ".DLT";
                    log.error("[DLT ms-ordenes] Fallo definitivo en topic={}, partition={}, offset={}, error={}",
                            record.topic(), record.partition(), record.offset(), ex.getMessage(), ex);

                    return new TopicPartition(dlt, record.partition());
                }
        );

        return new DefaultErrorHandler(recoverer, backoff);
    }

    // === TOPICS QUE ESTE MICROSERVICIO PRODUCE ===

    // ms-ordenes PRODUCE pago-pendiente
    @Bean
    public NewTopic pagoPendienteTopic(
            @Value("${app.kafka.topic.pago-pendiente}") String topic,
            @Value("${app.kafka.partitions:3}") int partitions,
            @Value("${app.kafka.replication:1}") short replication
    ) {
        return TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(replication)
                .build();
    }

    @Bean
    public NewTopic pagoPendienteDltTopic(
            @Value("${app.kafka.topic.pago-pendiente}") String topic,
            @Value("${app.kafka.partitions:3}") int partitions,
            @Value("${app.kafka.replication:1}") short replication
    ) {
        return TopicBuilder.name(topic + ".DLT")
                .partitions(partitions)
                .replicas(replication)
                .build();
    }

    // === TOPICS QUE ESTE MICROSERVICIO CONSUME ===

    @Bean
    public NewTopic pagoOkTopic(
            @Value("${app.kafka.topic.pago-ok}") String topic,
            @Value("${app.kafka.partitions:3}") int partitions,
            @Value("${app.kafka.replication:1}") short replication
    ) {
        return TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(replication)
                .build();
    }

    @Bean
    public NewTopic pagoErrorTopic(
            @Value("${app.kafka.topic.pago-error}") String topic,
            @Value("${app.kafka.partitions:3}") int partitions,
            @Value("${app.kafka.replication:1}") short replication
    ) {
        return TopicBuilder.name(topic)
                .partitions(partitions)
                .replicas(replication)
                .build();
    }
}