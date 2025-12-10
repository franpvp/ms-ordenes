package com.example.msordenes.application.kafka.producer;

import com.example.msordenes.application.dto.PagoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoPendienteProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private PagoPendienteProducer pagoPendienteProducer;

    private PagoDto pagoDtoValido;

    @BeforeEach
    void setUp() {
        pagoDtoValido = PagoDto.builder()
                .idOrden(10L)
                .idMetodoPago(1L)
                .monto(200000)
                .reprocesado(false)
                .build();
    }

    @Test
    void enviarTest() {
        // Arrange
        PagoDto event = pagoDtoValido;

        // Act
        pagoPendienteProducer.enviar(event);

        // Assert
        // Si en tu clase usas un topic configurado por @Value, puedes cambiar "anyString()" por el topic real
        verify(kafkaTemplate, times(1))
                .send(anyString(), eq(String.valueOf(event.getIdOrden())), eq(event));
    }

    @Test
    void enviarExceptionTest() {
        // Arrange
        PagoDto eventInvalido = PagoDto.builder()
                .idOrden(null) // provoca NPE cuando se usa como key, por ejemplo
                .idMetodoPago(1L)
                .monto(1000)
                .build();

        // Act + Assert
        assertThrows(NullPointerException.class,
                () -> pagoPendienteProducer.enviar(eventInvalido));

        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
    }
}
