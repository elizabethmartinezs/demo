package com.example.demo.rest;

public class DemoNoEncontradoException extends RuntimeException {

    public DemoNoEncontradoException(int id) {
        super("No existe el demo " + id);
    }
}
