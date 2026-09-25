package com.example.demo.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Bloque 5 - prueba unitaria con Mockito, sin contexto de Spring. */
@ExtendWith(MockitoExtension.class)
class DemoServiceTest {

    @Mock DemoRepository repository;
    @InjectMocks DemoService service;

    @Test
    void regresaDemoCuandoExiste() {
        when(repository.findById(1)).thenReturn(Optional.of(new Demo(1, "Uno"))); // Arrange
        Demo demo = service.buscar(1);                                           // Act
        assertThat(demo.name).isEqualTo("Uno");                                  // Assert
        verify(repository).findById(1);
    }

    @Test
    void lanzaExcepcionCuandoNoExiste() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.buscar(99))
                .isInstanceOf(DemoNoEncontradoException.class);
    }

    @Test
    void patchSoloCambiaLosCamposEnviados() {
        Demo existente = new Demo(1, "Uno");
        when(repository.findById(1)).thenReturn(Optional.of(existente));

        service.modificar(1, new Demo());   // cuerpo sin "name"

        assertThat(existente.name).isEqualTo("Uno");
    }
}
