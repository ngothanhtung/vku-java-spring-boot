package com.example.demo.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BackgroundService {
    @Async
    public void performBackgroundTask() {
        System.out.println("Executing task in thread: " + Thread.currentThread().getName());
        // Simulate a time-consuming task
        try {
            Thread.sleep(5000);  // Simulate delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Task completed in thread: " + Thread.currentThread().getName());
    }
}