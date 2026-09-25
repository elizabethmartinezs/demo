package com.example.demo.mvc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Módulo 1 - Herramientas de pruebas: MockMvc sobre SaludoRestController. */
@WebMvcTest(SaludoRestController.class)
class SaludoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void saludaPorNombre() throws Exception {
        mockMvc.perform(get("/api/hola").param("nombre", "Ana"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.mensaje").value("¡Hola, Ana!"));
    }
}
