package com.example.demo.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

/**
 * Bloques 3 y 5 - Controladores REST.
 * Mismos endpoints que en las diapositivas, pero delegando en DemoService.
 */
@RestController
@RequestMapping(path = "/api/demo")
public class DemoController {

    private final DemoService service;
    private final String location = "/api/demo/data/%d";

    public DemoController(DemoService service) {
        this.service = service;
    }

    @GetMapping(path = "/data")
    public List<Demo> getDemos() {
        return service.listar();
    }

    @GetMapping(path = "/data/{id}")
    public Demo getDemoById(@PathVariable(name = "id", required = true) int id) {
        return service.buscar(id);   // si no existe responde 404 (ver manejador abajo)
    }

    @GetMapping(path = "/data/query")
    public List<Demo> getDemosByParam(
            @RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "name", required = false) String name) {
        if (id != null) {
            return List.of(service.buscar(id));
        } else if (name != null) {
            return service.buscarPorNombre(name);
        }
        return service.listar();
    }

    @PostMapping(path = "/data",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> createDemo(@RequestBody Demo demo) {
        service.crear(demo);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, String.format(this.location, demo.id));
        return new ResponseEntity<>(new Message("Creado"), headers, HttpStatus.CREATED);
    }

    @PutMapping(path = "/data/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> updateDemo(
            @PathVariable(name = "id", required = true) int id,
            @RequestBody Demo update) {
        service.actualizar(id, update);
        return ResponseEntity.ok(new Message("Actualizado"));
    }

    @PatchMapping(path = "/data/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> patchDemo(
            @PathVariable(name = "id", required = true) int id,
            @RequestBody Demo cambios) {
        service.modificar(id, cambios);
        return ResponseEntity.ok(new Message("Modificado"));
    }

    @DeleteMapping(path = "/data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> deleteDemo(
            @PathVariable(name = "id", required = true) int id) {
        service.eliminar(id);
        return ResponseEntity.ok(new Message("Eliminado"));
    }

    /** Cualquier DemoNoEncontradoException se responde como 404 {"message":"No existe"}. */
    @ExceptionHandler(DemoNoEncontradoException.class)
    public ResponseEntity<Message> noEncontrado() {
        return new ResponseEntity<>(new Message("No existe"), HttpStatus.NOT_FOUND);
    }
}
