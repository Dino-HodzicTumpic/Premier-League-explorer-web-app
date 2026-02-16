package com.dino.plExplorer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloTest {

    @GetMapping("/hello")
    public String hello() {
       return "Hello from Premier league explorer app";
    }

    @GetMapping("/hello/{name}")
    public String helloName(@PathVariable String name){
        return "Hello " + name;
    }
}
