package com.sumitkrx.journal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {
    @GetMapping("/health-check")
    public String healthCheck(){
        return "OK";
    }
}
