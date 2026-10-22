package com.example.demo.graphql;

/**
 * Bloque 7 - GraphQL. Datos de entrada de crearLibro y actualizarLibro (input LibroInput).
 *   mutation { crearLibro(input: { titulo: "El túnel", autor: "Sabato", paginas: 160 }) { id } }
 */
public record LibroInput(String titulo, String autor, int paginas) {}
