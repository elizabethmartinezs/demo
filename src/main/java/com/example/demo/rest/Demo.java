package com.example.demo.rest;

/**
 * Recurso de ejemplo de los controladores REST.
 * Campos públicos para mantener el código de las diapositivas corto
 * (Jackson los serializa directamente).
 */
public class Demo {

    public int id;
    public String name;

    public Demo() {
        // requerido por Jackson para leer el JSON del @RequestBody
    }

    public Demo(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
