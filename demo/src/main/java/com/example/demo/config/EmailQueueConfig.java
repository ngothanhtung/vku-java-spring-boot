package com.example.demo.config;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.demo.models.EmailMessage;

@Configuration
@EnableAsync
@EnableScheduling
public class EmailQueueConfig {
	@Bean("emailQueue")
	public BlockingQueue<EmailMessage> emailQueue() {
		return new LinkedBlockingQueue<>(1000); // Max 1000 emails trong queue
	}

	@Bean("emailTaskExecutor")
	public Executor emailTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("email-sender-");
		executor.initialize();
		return executor;
	}
}
