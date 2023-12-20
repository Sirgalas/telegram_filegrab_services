package ru.sergalas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>("hello world", HttpStatus.OK);
    }
}
