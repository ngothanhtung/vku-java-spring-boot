package com.example.demo.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {
    @Scheduled(fixedRate = 10000)  // Run every 10 seconds
    public void performScheduledTask() {
        System.out.println("Scheduled task executed at: " + System.currentTimeMillis());
        // Simulate task
    }

    @Scheduled(cron = "0 0 12 * * ?")  // Run every day at 12PM
    public void dailyTask() {
        System.out.println("Daily task executed at 12 PM.");
    }
}