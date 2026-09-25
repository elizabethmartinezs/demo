# demo

Proyecto demo del curso **Java + Spring** (Axity). Spring Boot 4.0.5, Java 17+, Gradle (Groovy DSL) con Gradle Wrapper.

## Cómo abrirlo en Eclipse (Spring Tools)

1. `File > Import > Maven > Existing Maven Projects` y selecciona esta carpeta.
2. Espera a que Maven descargue las dependencias (primera vez, requiere internet).
3. Clic derecho en `DemoApplication` > `Run As > Spring Boot App`.
4. Pruebas: `./gradlew test` en terminal.

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

En `postman/` hay una colección con todas estas peticiones
(`File > Import` en Postman).

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
