package com.example.demo.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Bloque 4 - GraphQL.
 * Probar en http://localhost:8080/graphiql con:
 *   { libroPorId(id: "1") { titulo autor } }
 */
@Controller
public class LibroController {

    // Simulación de base de datos
    private static final List<Libro> libros = List.of(
            new Libro("1", "El Quijote", "Cervantes", 500),
            new Libro("2", "Ficciones", "Borges", 200));

    @QueryMapping
    public Libro libroPorId(@Argument String id) {
        return libros.stream()
                .filter(libro -> libro.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @QueryMapping
    public List<Libro> todosLosLibros() {
        return libros;
    }
}
