package com.hospita.sys.handler;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "logging-service")
public interface LoggingClient {

    @PostMapping("/logs")
    void saveLog(Logs request);

    @GetMapping("/logs")
    List<Logs> getAllLogs();
}
