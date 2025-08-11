package vku.apiservice.tutorials.infrastructure.services;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class ConcurrentTaskService {
    private final ThreadPoolTaskExecutor taskExecutor;

    public ConcurrentTaskService(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void executeTask() {
        taskExecutor.submit(() -> {
            System.out.println("Executing task in thread: " + Thread.currentThread().getName());
            // Simulate a task
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}