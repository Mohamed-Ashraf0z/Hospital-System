package com.example.essayfeedback.auth.service;

import java.util.List;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.essayfeedback.auth.entity.Logs;
import com.example.essayfeedback.auth.repo.logsrepo;

@Service
public class LogService {
    
    @Autowired
    private logsrepo logRepository;

    public void save(Logs log) {
        logRepository.save(log);
    }

    public List<Logs> getAll() {
        return logRepository.findAll();
    }
}
