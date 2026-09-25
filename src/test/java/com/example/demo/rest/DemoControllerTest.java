package com.example.demo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
}
