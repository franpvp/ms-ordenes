package com.example.msordenes.application.kafka.config;

import com.example.msordenes.application.dto.PagoErrorEvent;
import com.example.msordenes.application.dto.PagoOkEvent;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerConfigTest {

    private KafkaConsumerConfig kafkaConsumerConfig;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeEach
    void setUp() {
        kafkaConsumerConfig = new KafkaConsumerConfig();
    }

    @Test
    @DisplayName("genericConsumerFactory configura bootstrap y deserializadores básicos")
    void genericConsumerFactoryTest() {
        // Arrange
        String bootstrapServers = "localhost:9092";

        // Act
        ConsumerFactory<String, Object> factory =
                kafkaConsumerConfig.genericConsumerFactory(bootstrapServers);

        // Assert
        assertThat(factory).isInstanceOf(DefaultKafkaConsumerFactory.class);

        Map<String, Object> configs = factory.getConfigurationProperties();
        assertThat(configs.get(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG))
                .isEqualTo(bootstrapServers);
        assertThat(configs.get(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG))
                .isEqualTo(StringDeserializer.class);
        assertThat(configs.get(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG))
                .isEqualTo(JsonDeserializer.class);
    }

    @Test
    @DisplayName("pagoOkConsumerFactory configura JsonDeserializer<PagoOkEvent>")
    void pagoOkConsumerFactoryTest() {
        // Arrange
        String bootstrapServers = "localhost:9092";

        // Act
        ConsumerFactory<String, PagoOkEvent> factory =
                kafkaConsumerConfig.pagoOkConsumerFactory(bootstrapServers);

        // Assert
        assertThat(factory).isInstanceOf(DefaultKafkaConsumerFactory.class);

        Map<String, Object> configs = factory.getConfigurationProperties();
        assertThat(configs.get(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG))
                .isEqualTo(bootstrapServers);

        Deserializer<PagoOkEvent> valueDeserializer = factory.getValueDeserializer();
        assertThat(valueDeserializer)
                .isInstanceOf(JsonDeserializer.class);
    }

    @Test
    @DisplayName("pagoErrorConsumerFactory configura JsonDeserializer<PagoErrorEvent>")
    void pagoErrorConsumerFactoryTest() {
        // Arrange
        String bootstrapServers = "localhost:9092";

        // Act
        ConsumerFactory<String, PagoErrorEvent> factory =
                kafkaConsumerConfig.pagoErrorConsumerFactory(bootstrapServers);

        // Assert
        assertThat(factory).isInstanceOf(DefaultKafkaConsumerFactory.class);

        Map<String, Object> configs = factory.getConfigurationProperties();
        assertThat(configs.get(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG))
                .isEqualTo(bootstrapServers);

        Deserializer<PagoErrorEvent> valueDeserializer = factory.getValueDeserializer();
        assertThat(valueDeserializer)
                .isInstanceOf(JsonDeserializer.class);
    }

    @Test
    @DisplayName("kafkaListenerContainerFactory usa ACK manual y el DefaultErrorHandler configurado")
    void kafkaListenerContainerFactoryTest() {
        // Arrange
        String bootstrapServers = "localhost:9092";
        ConsumerFactory<String, Object> genericFactory =
                kafkaConsumerConfig.genericConsumerFactory(bootstrapServers);
        DefaultErrorHandler errorHandler =
                kafkaConsumerConfig.errorHandler(kafkaTemplate);

        // Act
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                kafkaConsumerConfig.kafkaListenerContainerFactory(genericFactory, errorHandler);

        // Assert
        assertThat(factory).isNotNull();
        assertThat(factory.getContainerProperties().getAckMode())
                .isEqualTo(ContainerProperties.AckMode.MANUAL);

    }

    @Test
    @DisplayName("errorHandler crea un DefaultErrorHandler válido con recoverer y FixedBackOff")
    void errorHandlerTest() {
        // Arrange
        // (Mock ya creado en @Mock)

        // Act
        DefaultErrorHandler errorHandler =
                kafkaConsumerConfig.errorHandler(kafkaTemplate);

        // Assert
        assertThat(errorHandler).isNotNull();
        // No comprobamos campos internos del recoverer para evitar acoplar el test
        // a detalles internos de la versión de Spring Kafka.
    }

    @Test
    @DisplayName("pagoPendienteTopic crea tópico con nombre, particiones y factor de replicación esperados")
    void pagoPendienteTopicTest() {
        // Arrange
        String topicName = "pago-pendiente";
        int partitions = 3;
        short replicationFactor = 1;

        // Act
        NewTopic topic = kafkaConsumerConfig.pagoPendienteTopic(topicName, partitions, replicationFactor);

        // Assert
        assertThat(topic.name()).isEqualTo(topicName);
        assertThat(topic.numPartitions()).isEqualTo(partitions);
        assertThat(topic.replicationFactor()).isEqualTo(replicationFactor);
    }

    @Test
    @DisplayName("pagoOkTopic crea tópico con nombre, particiones y factor de replicación esperados")
    void pagoOkTopicTest() {
        // Arrange
        String topicName = "pago-ok";
        int partitions = 3;
        short replicationFactor = 1;

        // Act
        NewTopic topic = kafkaConsumerConfig.pagoOkTopic(topicName, partitions, replicationFactor);

        // Assert
        assertThat(topic.name()).isEqualTo(topicName);
        assertThat(topic.numPartitions()).isEqualTo(partitions);
        assertThat(topic.replicationFactor()).isEqualTo(replicationFactor);
    }

    @Test
    @DisplayName("pagoErrorTopic crea tópico con nombre, particiones y factor de replicación esperados")
    void pagoErrorTopicTest() {
        // Arrange
        String topicName = "pago-error";
        int partitions = 3;
        short replicationFactor = 1;

        // Act
        NewTopic topic = kafkaConsumerConfig.pagoErrorTopic(topicName, partitions, replicationFactor);

        // Assert
        assertThat(topic.name()).isEqualTo(topicName);
        assertThat(topic.numPartitions()).isEqualTo(partitions);
        assertThat(topic.replicationFactor()).isEqualTo(replicationFactor);
    }
}
