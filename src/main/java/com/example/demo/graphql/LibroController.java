package com.example.demo.graphql;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Bloques 4 y 7 - GraphQL.
 * Probar en http://localhost:8080/graphiql con:
 *   { libroPorId(id: "1") { titulo autor } }
 */
@Controller
public class LibroController {

    // Simulación de base de datos (Bloque 7: lista mutable y segura para concurrencia).
    // No es static: cada instancia del controlador empieza con los mismos 6 libros.
    private final List<Libro> libros = new CopyOnWriteArrayList<>(List.of(
            new Libro("1", "El Quijote", "Cervantes", 500),
            new Libro("2", "Ficciones", "Borges", 200),
            new Libro("3", "Cien años de soledad", "García Márquez", 470),
            new Libro("4", "Pedro Páramo", "Rulfo", 130),
            new Libro("5", "La casa de los espíritus", "Allende", 450),
            new Libro("6", "Rayuela", "Cortázar", 600)));

    // Genera los ids de los libros nuevos: 7, 8, 9...
    private final AtomicInteger siguienteId = new AtomicInteger(7);

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

    /**
     * Bloque 7 - paginación, con las mismas reglas que en REST.
     *   { librosPaginados(pagina: 0, tamanio: 2) { contenido { titulo } totalPaginas } }
     */
    @QueryMapping
    public PaginaLibros librosPaginados(@Argument int pagina, @Argument int tamanio) {
        if (pagina < 0 || tamanio < 1) {
            throw new IllegalArgumentException("pagina debe ser >= 0 y tamanio >= 1");
        }
        int total = libros.size();
        int desde = (int) Math.min((long) pagina * tamanio, total);
        int hasta = Math.min(desde + tamanio, total);
        int totalPaginas = (int) Math.ceil((double) total / tamanio);
        return new PaginaLibros(new ArrayList<>(libros.subList(desde, hasta)), pagina, tamanio, total, totalPaginas);
    }

    /**
     * Bloque 7 - registro.
     *   mutation { crearLibro(input: { titulo: "El túnel", autor: "Sabato", paginas: 160 }) { id titulo } }
     */
    @MutationMapping
    public Libro crearLibro(@Argument LibroInput input) {
        Libro libro = new Libro(String.valueOf(siguienteId.getAndIncrement()),
                input.titulo(), input.autor(), input.paginas());
        libros.add(libro);
        return libro;
    }

    /**
     * Bloque 7 - edición. Libro es un record (inmutable): se reemplaza el elemento de la lista.
     * Regresa null si el libro no existe.
     *   mutation { actualizarLibro(id: "1", input: { titulo: "Don Quijote", autor: "Cervantes", paginas: 1200 }) { id titulo } }
     */
    @MutationMapping
    public Libro actualizarLibro(@Argument String id, @Argument LibroInput input) {
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).id().equals(id)) {
                Libro actualizado = new Libro(id, input.titulo(), input.autor(), input.paginas());
                libros.set(i, actualizado);
                return actualizado;
            }
        }
        return null;
    }

    /**
     * Bloque 7 - eliminación. true si lo eliminó, false si no existía.
     *   mutation { eliminarLibro(id: "2") }
     */
    @MutationMapping
    public boolean eliminarLibro(@Argument String id) {
        return libros.removeIf(libro -> libro.id().equals(id));
    }
}
