package com.example.demo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Bloque 5 - prueba de la capa web (slice) con @WebMvcTest + @MockitoBean. */
@WebMvcTest(DemoController.class)
class DemoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DemoService service;

    @Test
    void getRegresaElDemo() throws Exception {
        when(service.buscar(1)).thenReturn(new Demo(1, "Uno"));
        mockMvc.perform(get("/api/demo/data/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Uno"));
    }

    @Test
    void getResponde404CuandoNoExiste() throws Exception {
        when(service.buscar(99)).thenThrow(new DemoNoEncontradoException(99));
        mockMvc.perform(get("/api/demo/data/99"))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value("No existe"));
    }

    // Bloque 7 - si /data/paginado cayera en /data/{id}, "paginado" no se convertiría a int
    // y la respuesta sería 400: el 200 confirma que Spring prefiere la ruta literal.
    @Test
    void getPaginadoRegresaLaPagina() throws Exception {
        when(service.paginar(0, 2)).thenReturn(new PaginaDemo(
                List.of(new Demo(1, "Uno"), new Demo(2, "Dos")), 0, 2, 3, 2));
        mockMvc.perform(get("/api/demo/data/paginado").param("pagina", "0").param("tamanio", "2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.contenido.length()").value(2))
               .andExpect(jsonPath("$.totalPaginas").value(2));
        verify(service, never()).buscar(anyInt());
    }

    @Test
    void getPaginadoConTamanioCeroResponde400() throws Exception {
        when(service.paginar(0, 0)).thenThrow(new IllegalArgumentException("pagina debe ser >= 0 y tamanio >= 1"));
        mockMvc.perform(get("/api/demo/data/paginado").param("pagina", "0").param("tamanio", "0"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value("pagina debe ser >= 0 y tamanio >= 1"));
    }
}
