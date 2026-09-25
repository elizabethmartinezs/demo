package com.example.demo.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Módulo 1 - Interceptores. Paso 2: registrar el interceptor en Spring MVC. */
@Configuration
public class MiConfiguracion implements WebMvcConfigurer {

    @Autowired
    private SaludoInterceptor saludoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(saludoInterceptor); // aplica a TODAS las rutas
    }
}
