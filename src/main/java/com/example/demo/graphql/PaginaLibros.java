package com.example.demo.graphql;

import java.util.List;

/**
 * Bloque 7 - GraphQL. Una página de libros (type PaginaLibros en schema.graphqls).
 *   { librosPaginados(pagina: 0, tamanio: 2) { contenido { titulo } totalPaginas } }
 */
public record PaginaLibros(List<Libro> contenido, int pagina, int tamanio, int totalElementos, int totalPaginas) {}
