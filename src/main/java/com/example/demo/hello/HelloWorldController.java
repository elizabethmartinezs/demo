package com.example.demo.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @Value("${hello.app.name}")
    private String appName;

    // http://localhost:8080/json  ->  {"hello":"Hello World!!!"}
    @GetMapping(path = "/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public Hello helloJson() {
        return new Hello("Hello World!!!");
    }

    // http://localhost:8080/app  ->  valor leído de application.properties
    @GetMapping(path = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
    public Hello helloAppName() {
        return new Hello(this.appName);
    }
}
