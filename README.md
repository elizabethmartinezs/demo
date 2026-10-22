# demo

Proyecto demo del curso **Java + Spring** (Axity). Spring Boot 4.0.5, Java 21, Gradle (Groovy DSL) con Gradle Wrapper.

## Cómo abrirlo en IntelliJ IDEA

1. `File > Open` y selecciona esta carpeta (o el `build.gradle`) > `Open as Project`.
2. Espera a que IntelliJ importe el proyecto Gradle y descargue las dependencias
   (primera vez, requiere internet). Verifica en `File > Project Structure` que el SDK sea Java 21.
3. Abre `DemoApplication` y pulsa la flecha verde junto a `main` > `Run`.
4. Pruebas: clic derecho en `src/test/java` > `Run 'All Tests'`, o `./gradlew test` en la terminal
   integrada (`View > Tool Windows > Terminal`).
5. Peticiones: abre `http/demo.http` y pulsa la flecha verde de cada petición
   (cliente HTTP de IntelliJ), o importa la colección de `postman/` en Postman.

> Si usas IntelliJ IDEA con soporte de Spring (Ultimate), el asistente
> `File > New > Project > Spring Boot` genera el mismo proyecto que start.spring.io.

## Un commit por bloque de la presentación

El historial de Git reproduce el orden del curso. Para mostrar el proyecto
en el estado de un bloque:

```bash
git checkout bloque-3-rest      # ver el proyecto al terminar el bloque 3
git checkout main               # volver a la versión completa
```

| Etiqueta | Bloque | Qué agrega |
|---|---|---|
| `bloque-2.1-proyecto-base` | Primer proyecto | Proyecto de Initializr, solo Spring Web |
| `bloque-2.2-primer-endpoint` | Primer proyecto | `/json`, `/app`, `application.properties`, `@Value` |
| `modulo-1-ejemplos` | Módulo 1 | Servlet, `SaludoRestController`, interceptor |
| `bloque-3-rest` | Controladores REST | CRUD `/api/demo/data` con los 5 verbos |
| `bloque-4-graphql` | GraphQL | Starter, `schema.graphqls`, `LibroController` |
| `bloque-5-pruebas-unitarias` | Pruebas unitarias | `DemoService`, Mockito, `@WebMvcTest` |
| `bloque-6-pruebas-integracion` | Pruebas de integración | `@SpringBootTest` + `@AutoConfigureMockMvc` |
| `bloque-7-paginacion-y-mutaciones` | Paginación y mutaciones | Paginación REST y GraphQL, `@MutationMapping`, `@GraphQlTest` |

## Endpoints (http://localhost:8080)

| Método | Ruta | Ejemplo |
|---|---|---|
| GET | `/json` | `{"hello":"Hello World!!!"}` |
| GET | `/app` | Lee `hello.app.name` de `application.properties` |
| GET | `/saludo?nombre=Ana` | Servlet (texto plano) |
| GET | `/api/hola?nombre=Ana` | `{"mensaje":"¡Hola, Ana!"}` |
| GET | `/api/demo/data` | Lista de demos |
| GET | `/api/demo/data/1` | Un demo; 404 si no existe |
| GET | `/api/demo/data/query?name=dos` | Filtro por parámetro |
| POST | `/api/demo/data` | Cuerpo `{"id":4,"name":"Cuatro"}` → 201 + `Location` |
| PUT | `/api/demo/data/1` | Cuerpo `{"name":"Uno actualizado"}` |
| PATCH | `/api/demo/data/1` | Cuerpo `{"name":"Nuevo nombre"}` |
| DELETE | `/api/demo/data/1` | 200 `{"message":"Eliminado"}` |
| POST | `/graphql` | Cuerpo `{"query":"{ libroPorId(id: \"1\") { titulo } }"}` |
| — | `/graphiql` | Consola GraphQL en el navegador |

En `http/demo.http` están todas estas peticiones para el cliente HTTP de IntelliJ,
y en `postman/` la misma colección para Postman (`File > Import`).

## Bloque 7: paginación y mutaciones

Paginación REST (la página empieza en 0; una página fuera de rango regresa `contenido` vacío):

| Método | Ruta | Ejemplo |
|---|---|---|
| GET | `/api/demo/data/paginado?pagina=0&tamanio=2` | `{"contenido":[...],"pagina":0,"tamanio":2,"totalElementos":3,"totalPaginas":2}` |
| GET | `/api/demo/data/paginado?pagina=0&tamanio=0` | 400 `{"message":"pagina debe ser >= 0 y tamanio >= 1"}` |

La ruta literal `/data/paginado` tiene prioridad sobre `/data/{id}`, así que `/api/demo/data/1` sigue funcionando.

GraphQL (`POST /graphql` o la consola `/graphiql`). Los nombres GraphQL no admiten ñ ni acentos, por eso `tamanio`:

```graphql
{ librosPaginados(pagina: 0, tamanio: 2) { contenido { id titulo } totalElementos totalPaginas } }

mutation { crearLibro(input: { titulo: "El túnel", autor: "Sabato", paginas: 160 }) { id titulo } }

mutation { actualizarLibro(id: "1", input: { titulo: "Don Quijote de la Mancha", autor: "Cervantes", paginas: 1200 }) { id titulo } }

mutation { eliminarLibro(id: "2") }   # true si lo eliminó, false si no existía
```

`crearLibro` asigna ids desde 7; `actualizarLibro` regresa `null` si el id no existe.
Las pruebas están en `LibroControllerTest` (`@GraphQlTest` + `GraphQlTester`).

## Comandos de Gradle

```bash
./gradlew bootRun                          # arranca la aplicación
./gradlew test                             # todas las pruebas
./gradlew test --tests DemoServiceTest     # una sola clase
./gradlew test --rerun                     # repite las pruebas aunque no haya cambios
./gradlew build                            # compila, prueba y genera el JAR en build/libs
```

En Windows se usa `gradlew.bat` en lugar de `./gradlew`.
El reporte de pruebas queda en `build/reports/tests/test/index.html`.

## Notas

* Los datos viven en memoria: se reinician cada vez que arranca la aplicación.
* A partir del bloque 5, `DemoController` delega en `DemoService`; un demo
  inexistente responde `404 {"message":"No existe"}`.
* JSP no se incluye: requiere empaquetado War (el proyecto es Jar).
