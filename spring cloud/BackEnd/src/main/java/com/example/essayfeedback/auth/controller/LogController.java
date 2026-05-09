package com.example.essayfeedback.auth.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.essayfeedback.auth.entity.Logs;
import com.example.essayfeedback.auth.service.LogService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/logs")
public class LogController {

    @Autowired
    private LogService service;

    @PostMapping
    public void save(@RequestBody Logs request) {
        service.save(request);
    }

    @GetMapping
    public List<Logs> getAll() {
        return service.getAll();
    }
}
