package com.example.demo.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Módulo 1 - Interceptores. Paso 1: implementar HandlerInterceptor. */
@Component
public class SaludoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        System.out.println("¡Hola! Alguien llamó a la ruta: "
                + request.getRequestURI());
        return true; // true: la petición sigue al controlador
    }
}
