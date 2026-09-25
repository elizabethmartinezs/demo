package com.example.demo.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Módulo 1 - Controladores con vista.
 * http://localhost:8080/vista/hola?nombre=Ana  ->  página HTML (templates/saludo.html)
 */
@Controller
public class SaludoVistaController {

    @GetMapping("/vista/hola")
    public String saludar(@RequestParam(defaultValue = "Mundo") String nombre, Model model) {
        model.addAttribute("nombre", nombre);
        return "saludo";   // nombre de la vista, no el contenido de la respuesta
    }
}
