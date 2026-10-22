package com.example.demo.rest;

import java.util.List;

/**
 * Bloque 7 - una página de demos más los datos para navegar entre páginas.
 * GET http://localhost:8080/api/demo/data/paginado?pagina=0&tamanio=2
 */
public record PaginaDemo(List<Demo> contenido, int pagina, int tamanio, int totalElementos, int totalPaginas) {
}
