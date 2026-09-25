package com.example.demo.rest;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class InMemoryDemoRepository implements DemoRepository {

    private final List<Demo> data = new CopyOnWriteArrayList<>(List.of(
            new Demo(1, "Uno"),
            new Demo(2, "Dos"),
            new Demo(3, "Tres")));

    @Override
    public List<Demo> findAll() {
        return data;
    }

    @Override
    public Optional<Demo> findById(int id) {
        return data.stream().filter(d -> d.id == id).findFirst();
    }

    @Override
    public Demo save(Demo demo) {
        deleteById(demo.id);
        data.add(demo);
        return demo;
    }

    @Override
    public boolean deleteById(int id) {
        return data.removeIf(d -> d.id == id);
    }
}
