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

	/*
	 * ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor()
	 * Tạo một thread pool executor để xử lý các task bất đồng bộ
	 * Đây là implementation của Spring cho ThreadPoolExecutor của Java
	 * 
	 * 
	 * executor.setCorePoolSize(2)
	 * Core Pool Size = 2: Số lượng thread tối thiểu luôn được duy trì trong pool
	 * Ngay cả khi không có task nào, vẫn có 2 thread sẵn sàng
	 * 
	 * 
	 * executor.setMaxPoolSize(5)
	 * Max Pool Size = 5: Số lượng thread tối đa có thể tạo ra
	 * Khi có nhiều task, pool có thể mở rộng từ 2 lên tối đa 5 thread
	 * 
	 * 
	 * executor.setQueueCapacity(100)
	 * Queue Capacity = 100: Số lượng task tối đa có thể chờ trong hàng đợi
	 * Khi tất cả thread đều bận, task mới sẽ được đưa vào queue chờ
	 * 
	 * 
	 * executor.setThreadNamePrefix("email-sender-")
	 * Đặt tiền tố cho tên các thread: email-sender-1, email-sender-2, v.v.
	 * Giúp dễ dàng debug và monitoring
	 */
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
