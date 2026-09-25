package com.example.demo.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Bloque 3 - Controladores REST.
 * Versión de las diapositivas: la lista vive en el controlador.
 * (En el bloque de pruebas unitarias se refactoriza a DemoService.)
 */
@RestController
@RequestMapping(path = "/api/demo")
public class DemoController {

    private final List<Demo> data = new CopyOnWriteArrayList<>(List.of(
            new Demo(1, "Uno"),
            new Demo(2, "Dos"),
            new Demo(3, "Tres")));

    private final String location = "/api/demo/data/%d";

    // GET http://localhost:8080/api/demo/data
    @GetMapping(path = "/data")
    public List<Demo> getDemos() {
        return this.data;
    }

    // GET http://localhost:8080/api/demo/data/1
    @GetMapping(path = "/data/{id}")
    public Demo getDemoById(@PathVariable(name = "id", required = true) int id) {
        return this.data.stream()
                .filter(d -> d.id == id)
                .findFirst()
                .orElse(new Demo(-1, "No existe"));
    }

    // GET http://localhost:8080/api/demo/data/query?name=dos
    @GetMapping(path = "/data/query")
    public List<Demo> getDemosByParam(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "name", required = false) String name) {
        List<Demo> list;
        if (id != null) {
            list = this.data.stream()
                    .filter(d -> d.id == id)
                    .collect(Collectors.toList());
        } else if (name != null) {
            list = this.data.stream()
                    .filter(d -> d.name.equalsIgnoreCase(name))
                    .collect(Collectors.toList());
        } else {
            list = getDemos();
        }
        return list;
    }

    // POST http://localhost:8080/api/demo/data   cuerpo: {"id": 4, "name": "Cuatro"}
    @PostMapping(path = "/data",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> createDemo(@RequestBody Demo demo) {
        this.data.add(demo);
        Message message = new Message("Creado");
        HttpHeaders headers = new HttpHeaders();
        String path = String.format(this.location, demo.id);
        headers.add(HttpHeaders.LOCATION, path);
        return new ResponseEntity<>(message, headers, HttpStatus.CREATED);
    }

    // PUT http://localhost:8080/api/demo/data/1   cuerpo: {"name": "Uno actualizado"}
    @PutMapping(path = "/data/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> updateDemo(
            @PathVariable(name = "id", required = true) int id,
            @RequestBody Demo update) {
        Demo demo = this.data.stream()
                .filter(d -> d.id == id).findFirst().orElse(null);
        ResponseEntity<Message> response;
        if (demo != null) {
            demo.setName(update.name);
            response = ResponseEntity.ok(new Message("Actualizado"));
        } else {
            response = new ResponseEntity<>(new Message("No existe"),
                    HttpStatus.NOT_FOUND);
        }
        return response;
    }

    // PATCH http://localhost:8080/api/demo/data/1   cuerpo: {"name": "Nuevo nombre"}
    @PatchMapping(path = "/data/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> patchDemo(
            @PathVariable(name = "id", required = true) int id,
            @RequestBody Demo cambios) {
        Demo demo = this.data.stream()
                .filter(d -> d.id == id).findFirst().orElse(null);
        if (demo == null) {
            return new ResponseEntity<>(new Message("No existe"),
                    HttpStatus.NOT_FOUND);
        }
        if (cambios.name != null) {   // solo se aplican los campos enviados
            demo.setName(cambios.name);
        }
        return ResponseEntity.ok(new Message("Modificado"));
    }

    // DELETE http://localhost:8080/api/demo/data/1
    @DeleteMapping(path = "/data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> deleteDemo(
            @PathVariable(name = "id", required = true) int id) {
        Demo demo = this.data.stream()
                .filter(d -> d.id == id).findFirst().orElse(null);
        ResponseEntity<Message> response;
        if (demo != null) {
            this.data.remove(demo);
            response = ResponseEntity.ok(new Message("Eliminado"));
        } else {
            response = new ResponseEntity<>(new Message("No existe"),
                    HttpStatus.NOT_FOUND);
        }
        return response;
    }
}
