package com.example.demo.controllers;

import com.example.demo.services.BackgroundService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackgroundController {
    private final BackgroundService backgroundService;

    public BackgroundController(BackgroundService backgroundService) {
        this.backgroundService = backgroundService;
    }

    @GetMapping("/start-task")
    public String startBackgroundTask() {
        backgroundService.performBackgroundTask();
        return "Task is being processed in the background.";
    }
}
