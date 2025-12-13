package com.example.msordenes.application.kafka.consumer;

import com.example.msordenes.application.dto.PagoOkEvent;
import com.example.msordenes.application.exception.OrdenNoEncontradaException;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoOkConsumerTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private Acknowledgment acknowledgment;

    @InjectMocks
    private PagoOkConsumer pagoOkConsumer;

    private Long idOrdenOk;
    private PagoOkEvent pagoOkEvent;
    private OrdenEntity ordenPendiente;

    @BeforeEach
    void setUp() {
        idOrdenOk = 10L;

        pagoOkEvent = PagoOkEvent.builder()
                .idOrden(idOrdenOk)
                .idPago(50L)
                .monto(200000)
                .fechaOk(LocalDateTime.now())
                .build();

        ordenPendiente = new OrdenEntity();
        ordenPendiente.setId(idOrdenOk);
        ordenPendiente.setEstadoOrden("PAGO_PENDIENTE");
        ordenPendiente.setFechaOrden(LocalDateTime.now());
    }

}
