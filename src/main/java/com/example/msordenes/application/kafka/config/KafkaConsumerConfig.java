package com.example.msordenes.application.kafka.config;

import com.example.msordenes.application.dto.PagoOkEvent;
import com.example.msordenes.application.dto.PagoErrorEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, Object> genericConsumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        JsonDeserializer<Object> deserializer = new JsonDeserializer<>();
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new org.apache.kafka.common.serialization.StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> genericConsumerFactory,
            DefaultErrorHandler errorHandler
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(genericConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, Object> template) {

        var backoff = new FixedBackOff(1000L, 3L);

        var recoverer = new DeadLetterPublishingRecoverer(
                template,
                (record, ex) -> {
                    String dlt = record.topic() + ".DLT";
                    log.error("[DLT ms-ordenes] error definitivo topic={}, partition={}, offset={}, error={}",
                            record.topic(), record.partition(), record.offset(), ex.getMessage(), ex);
                    return new TopicPartition(dlt, record.partition());
                }
        );

        return new DefaultErrorHandler(recoverer, backoff);
    }

    @Bean
    public ConsumerFactory<String, PagoOkEvent> pagoOkConsumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);

        JsonDeserializer<PagoOkEvent> deserializer =
                new JsonDeserializer<>(PagoOkEvent.class, false);

        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new org.apache.kafka.common.serialization.StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PagoOkEvent> pagoOkKafkaListenerFactory(
            ConsumerFactory<String, PagoOkEvent> pagoOkConsumerFactory,
            DefaultErrorHandler errorHandler
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, PagoOkEvent>();
        factory.setConsumerFactory(pagoOkConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PagoErrorEvent> pagoErrorConsumerFactory(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);

        JsonDeserializer<PagoErrorEvent> deserializer =
                new JsonDeserializer<>(PagoErrorEvent.class, false);
        deserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                props,
                new org.apache.kafka.common.serialization.StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PagoErrorEvent> pagoErrorKafkaListenerFactory(
            ConsumerFactory<String, PagoErrorEvent> pagoErrorConsumerFactory,
            DefaultErrorHandler errorHandler
    ) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, PagoErrorEvent>();
        factory.setConsumerFactory(pagoErrorConsumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }

    @Bean
    public NewTopic pagoPendienteTopic(
            @Value("${app.kafka.topic.pago-pendiente}") String topic,
            @Value("${app.kafka.partitions:3}") int partitions,
            @Value("${app.kafka.replication:1}") short replication
    ) {
        return TopicBuilder.name(topic).partitions(partitions).replicas(replication).build();
    }

    @Bean
    public NewTopic pagoPendienteDltTopic(
            @Value("${app.kafka.topic.pago-pendiente}") String topic,
            @Value("${app.kafka.partitions:3}") int partitions,
            @Value("${app.kafka.replication:1}") short replication
    ) {
        return TopicBuilder.name(topic + ".DLT").partitions(partitions).replicas(replication).build();
    }

    @Bean
    public NewTopic pagoOkTopic(
            @Value("${app.kafka.topic.pago-ok}") String topic,
            @Value("${app.kafka.partitions:3}") int partitions,
            @Value("${app.kafka.replication:1}") short replication
    ) {
        return TopicBuilder.name(topic).partitions(partitions).replicas(replication).build();
    }

    @Bean
    public NewTopic pagoErrorTopic(
            @Value("${app.kafka.topic.pago-error}") String topic,
            @Value("${app.kafka.partitions:3}") int partitions,
            @Value("${app.kafka.replication:1}") short replication
    ) {
        return TopicBuilder.name(topic).partitions(partitions).replicas(replication).build();
    }
}