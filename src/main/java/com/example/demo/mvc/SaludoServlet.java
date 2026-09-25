package com.example.demo.mvc;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Módulo 1 - Servlets.
 * http://localhost:8080/saludo?nombre=Ana
 * Se registra gracias a @ServletComponentScan en DemoApplication.
 */
@WebServlet("/saludo")
public class SaludoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String nombre = req.getParameter("nombre");
        resp.setContentType("text/plain;charset=UTF-8");
        resp.getWriter().println("¡Hola, " + (nombre != null ? nombre : "Mundo") + "!");
    }
}
