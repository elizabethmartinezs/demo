package com.example.demo.rest;

import java.util.List;
import java.util.Optional;

/** Acceso a datos de Demo. En el curso se implementa en memoria. */
public interface DemoRepository {

    List<Demo> findAll();

    Optional<Demo> findById(int id);

    Demo save(Demo demo);

    boolean deleteById(int id);
}
