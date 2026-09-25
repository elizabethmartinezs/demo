package com.example.demo.mvc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Módulo 1 - Controladores.
 * http://localhost:8080/api/hola?nombre=Ana  ->  {"mensaje":"¡Hola, Ana!"}
 */
@RestController
@RequestMapping("/api")
public class SaludoRestController {

    public record Saludo(String mensaje) {}

    @GetMapping("/hola")
    public Saludo saludar(
            @RequestParam(name = "nombre", required = false,
                          defaultValue = "Mundo") String nombre) {
        return new Saludo("¡Hola, " + nombre + "!");
    }
}
