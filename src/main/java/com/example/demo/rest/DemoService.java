package com.example.demo.rest;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    /**
     * Bloque 7 - paginación. La página empieza en 0; una página fuera de rango
     * regresa contenido vacío. Ejemplo: paginar(0, 2) con 3 demos -> 2 demos, totalPaginas 2.
     */
    public PaginaDemo paginar(int pagina, int tamanio) {
        if (pagina < 0 || tamanio < 1) {
            throw new IllegalArgumentException("pagina debe ser >= 0 y tamanio >= 1");
        }
        List<Demo> todos = repository.findAll();
        int total = todos.size();
        int desde = (int) Math.min((long) pagina * tamanio, total);   // long: evita desbordamiento
        int hasta = Math.min(desde + tamanio, total);
        int totalPaginas = (int) Math.ceil((double) total / tamanio);
        // copia de la sublista: subList es solo una vista de la lista original
        return new PaginaDemo(new ArrayList<>(todos.subList(desde, hasta)), pagina, tamanio, total, totalPaginas);
    }
}
