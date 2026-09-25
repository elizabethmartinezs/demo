package com.example.demo.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Bloque 4 - GraphQL. Segundo esquema.
 * Probar en http://localhost:8080/graphiql con:
 *   { todosLosLibros { titulo } todosLosProductos { nombre precio } }
 */
@Controller
public class ProductoController {

    // Simulación de base de datos
    private static final List<Producto> productos = List.of(
            new Producto("1", "Laptop", 18500.0, 10),
            new Producto("2", "Monitor", 4200.0, 25));

    @QueryMapping
    public Producto productoPorId(@Argument String id) {
        return productos.stream()
                .filter(p -> p.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    @QueryMapping
    public List<Producto> todosLosProductos() {
        return productos;
    }
}
