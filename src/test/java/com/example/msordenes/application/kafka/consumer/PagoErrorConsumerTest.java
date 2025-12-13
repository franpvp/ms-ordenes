package com.example.msordenes.application.kafka.consumer;

import com.example.msordenes.application.dto.PagoErrorEvent;
import com.example.msordenes.application.dto.ReintentoResponseDto;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
import com.example.msordenes.application.feign.ReintentoFeignClient;
import com.example.msordenes.application.jpa.entity.OrdenEntity;
import com.example.msordenes.application.jpa.repository.OrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoErrorConsumerTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private ReintentoFeignClient reintentoFeignClient;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private PagoErrorConsumer pagoErrorConsumer;

    private Long idOrdenError;
    private PagoErrorEvent eventoConexionError;
    private PagoErrorEvent eventoNegocioError;
    private OrdenEntity ordenPendiente;
    private ReintentoResponseDto reintentoAgotado;
    private ReintentoResponseDto reintentoPendiente;

    @BeforeEach
    void setUp() {
        idOrdenError = 10L;

        eventoConexionError = PagoErrorEvent.builder()
                .idOrden(idOrdenError)
                .idPago(50L)
                .motivoError("ERROR CONEXION")
                .fechaError(LocalDateTime.now())
                .build();

        eventoNegocioError = PagoErrorEvent.builder()
                .idOrden(idOrdenError)
                .idPago(51L)
                .motivoError("MONTO INVALIDO")
                .fechaError(LocalDateTime.now())
                .build();

        ordenPendiente = new OrdenEntity();
        ordenPendiente.setId(idOrdenError);
        ordenPendiente.setEstadoOrden("PAGO_PENDIENTE");
        ordenPendiente.setFechaOrden(LocalDateTime.now());

        reintentoAgotado = ReintentoResponseDto.builder()
                .idPago(50L)
                .intentoActual(3)
                .maxIntentos(3)
                .estado("AGOTADO")
                .mensaje("Intentos agotados")
                .build();

        reintentoPendiente = ReintentoResponseDto.builder()
                .idPago(50L)
                .intentoActual(1)
                .maxIntentos(3)
                .estado("PENDIENTE")
                .mensaje("Se volverá a intentar")
                .build();
    }

    @Test
    void escucharPagoErrorConexionPendienteTest() {
        // Arrange
        when(reintentoFeignClient.reintentar(eventoConexionError))
                .thenReturn(reintentoPendiente);

        // Act
        pagoErrorConsumer.escucharPagoError(eventoConexionError, acknowledgment);

        // Assert
        verify(reintentoFeignClient, times(1)).reintentar(eventoConexionError);
        verify(ordenRepository, never()).findById(anyLong());
        verify(ordenRepository, never()).save(any(OrdenEntity.class));
        verify(acknowledgment, times(1)).acknowledge();
    }

}
