package com.example.demo.rest;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Bloque 5 - la lógica sale del controlador a un servicio.
 * La dependencia llega por el constructor: en la prueba unitaria se reemplaza por un mock.
 */
@Service
public class DemoService {

    private final DemoRepository repository;

    public DemoService(DemoRepository repository) {
        this.repository = repository;
    }

    public List<Demo> listar() {
        return repository.findAll();
    }

    public Demo buscar(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new DemoNoEncontradoException(id));
    }

    public List<Demo> buscarPorNombre(String name) {
        return repository.findAll().stream()
                .filter(d -> d.name.equalsIgnoreCase(name))
                .toList();
    }

    public Demo crear(Demo demo) {
        return repository.save(demo);
    }

    /** PUT: reemplaza los datos del demo. */
    public Demo actualizar(int id, Demo update) {
        Demo demo = buscar(id);
        demo.setName(update.name);
        return demo;
    }

    /** PATCH: solo aplica los campos enviados. */
    public Demo modificar(int id, Demo cambios) {
        Demo demo = buscar(id);
        if (cambios.name != null) {
            demo.setName(cambios.name);
        }
        return demo;
    }

    public void eliminar(int id) {
        if (!repository.deleteById(id)) {
            throw new DemoNoEncontradoException(id);
        }
    }
}
