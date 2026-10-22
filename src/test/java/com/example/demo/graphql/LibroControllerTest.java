package com.example.demo.graphql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.graphql.test.autoconfigure.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Bloque 7 - prueba de la capa GraphQL (slice) con @GraphQlTest + GraphQlTester.
 * Las mutations cambian la lista del controlador; @DirtiesContext crea un contexto
 * (y un LibroController con los 6 libros) nuevo después de cada prueba.
 */
@GraphQlTest(LibroController.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LibroControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void librosPaginadosRegresaLaPrimeraPagina() {
        graphQlTester.document("{ librosPaginados(pagina: 0, tamanio: 2) { contenido { id titulo autor paginas } totalPaginas } }")
                .execute()
                .path("librosPaginados.contenido").entityList(Libro.class).hasSize(2)
                .path("librosPaginados.totalPaginas").entity(Integer.class).isEqualTo(3);
    }

    @Test
    void crearLibroRegresaElLibroConId() {
        graphQlTester.document("""
                        mutation { crearLibro(input: { titulo: "El túnel", autor: "Sabato", paginas: 160 }) { id titulo } }
                        """)
                .execute()
                .path("crearLibro.id").entity(String.class).isEqualTo("7")
                .path("crearLibro.titulo").entity(String.class).isEqualTo("El túnel");
    }

    @Test
    void actualizarLibroCambiaElTitulo() {
        graphQlTester.document("""
                        mutation { actualizarLibro(id: "1", input: { titulo: "Don Quijote", autor: "Cervantes", paginas: 1200 }) { titulo } }
                        """)
                .execute()
                .path("actualizarLibro.titulo").entity(String.class).isEqualTo("Don Quijote");

        graphQlTester.document("{ libroPorId(id: \"1\") { titulo } }")
                .execute()
                .path("libroPorId.titulo").entity(String.class).isEqualTo("Don Quijote");
    }

    @Test
    void eliminarLibroYDespuesYaNoExiste() {
        graphQlTester.document("mutation { eliminarLibro(id: \"2\") }")
                .execute()
                .path("eliminarLibro").entity(Boolean.class).isEqualTo(true);

        graphQlTester.document("{ libroPorId(id: \"2\") { titulo } }")
                .execute()
                .path("libroPorId").valueIsNull();
    }
}
