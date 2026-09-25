package com.example.demo.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Bloque 6 - prueba de integración: contexto completo, sin mocks. */
@SpringBootTest
@AutoConfigureMockMvc
class DemoApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void creaYConsultaUnDemo() throws Exception {
        mockMvc.perform(post("/api/demo/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 10, \"name\": \"Diez\"}"))
               .andExpect(status().isCreated())
               .andExpect(header().exists(HttpHeaders.LOCATION));
        mockMvc.perform(get("/api/demo/data/10"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Diez"));
    }
}
