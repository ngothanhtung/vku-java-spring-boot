package com.example.demo.services;

import com.example.demo.models.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

@Service
public class EmailQueueService {
    private static final Logger logger = LoggerFactory.getLogger(EmailQueueService.class);

    // Đây là loại queue (có tính chất FIFO - First In First Out)
    @Qualifier("emailQueue")
    private final BlockingQueue<EmailMessage> emailQueue;

    public EmailQueueService(@Qualifier("emailQueue") BlockingQueue<EmailMessage> emailQueue) {
        this.emailQueue = emailQueue;
    }

    // Lấy kích thước queue hiện tại
    public int getQueueSize() {
        return emailQueue.size();
    }

    // Process email queue - chạy mỗi 5 giây
    @Scheduled(fixedDelay = 5000)
    public void processEmailQueue() {
        processNextEmail();
    }

    @Async("emailTaskExecutor")
    public void processNextEmail() {
        // Lấy email tiếp theo từ queue và gửi
        logger.info("Processing next email from the queue...");
        if (emailQueue.isEmpty()) {
            logger.info("Email queue is empty.");
            return;
        }

        EmailMessage emailMessage = emailQueue.poll();
        if (emailMessage != null) {
            // Giả lập gửi email
            // Thực hiện gửi email ở đây (ví dụ: sử dụng JavaMailSender)
            // ...
            // Dùng JavaMailSender để gửi email
            // ...
            try {
                Thread.sleep(1000); // Giả lập thời gian gửi email
            } catch (InterruptedException e) {
                logger.error("Error while sending email: {}", e.getMessage());
            }
            logger.info("🚀 Email sent successfully to: {}", emailMessage.getTo());
        } else {
            logger.info("No emails to process in the queue.");
        }
    }

    // Thêm email vào queue
    public boolean addToQueue(EmailMessage emailMessage) {
        try {
            boolean added = emailQueue.offer(emailMessage);
            if (added) {
                logger.info("Email added to queue: {}", emailMessage);
            } else {
                logger.warn("Failed to add email to queue (queue might be full): {}", emailMessage);
            }
            return added;
        } catch (Exception e) {
            logger.error("Error adding email to queue: {}", emailMessage, e);
            return false;
        }
    }
}
