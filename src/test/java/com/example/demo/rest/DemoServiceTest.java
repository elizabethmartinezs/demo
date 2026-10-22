package com.example.demo.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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

    // Bloque 7 - paginación con 3 demos en el repositorio
    private void tresDemos() {
        when(repository.findAll()).thenReturn(List.of(
                new Demo(1, "Uno"), new Demo(2, "Dos"), new Demo(3, "Tres")));
    }

    @Test
    void paginaCeroDeTamanioDos() {
        tresDemos();
        PaginaDemo pagina = service.paginar(0, 2);
        assertThat(pagina.contenido()).hasSize(2);
        assertThat(pagina.totalPaginas()).isEqualTo(2);
        assertThat(pagina.totalElementos()).isEqualTo(3);
    }

    @Test
    void paginaUnoRegresaElResto() {
        tresDemos();
        PaginaDemo pagina = service.paginar(1, 2);
        assertThat(pagina.contenido()).hasSize(1);
        assertThat(pagina.contenido().get(0).name).isEqualTo("Tres");
    }

    @Test
    void paginaFueraDeRangoRegresaContenidoVacio() {
        tresDemos();
        assertThat(service.paginar(5, 2).contenido()).isEmpty();
    }

    @Test
    void tamanioCeroLanzaExcepcion() {
        assertThatThrownBy(() -> service.paginar(0, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
