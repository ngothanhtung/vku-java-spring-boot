package com.example.demo.services;

import com.example.demo.models.EmailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmailRedisQueueService {

    private static final Logger logger = LoggerFactory.getLogger(EmailRedisQueueService.class);
    private static final String EMAIL_QUEUE_KEY = "email:queue";

    private final RedisTemplate<String, EmailMessage> redisTemplate;

    public EmailRedisQueueService(@Qualifier("emailRedisTemplate") RedisTemplate<String, EmailMessage> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Thêm email vào Redis queue
     *
     * @param emailMessage Email cần gửi
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addToQueue(EmailMessage emailMessage) {
        try {
            // Sử dụng LPUSH để thêm email vào đầu list (FIFO khi dùng RPOP)
            Long queueSize = redisTemplate.opsForList().leftPush(EMAIL_QUEUE_KEY, emailMessage);
            logger.info("Email added to Redis queue: {}. Current queue size: {}", emailMessage, queueSize);
            return true;
        } catch (Exception e) {
            logger.error("Failed to add email to Redis queue: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Lấy kích thước queue hiện tại
     *
     * @return Số lượng email trong queue
     */
    public long getQueueSize() {
        try {
            Long size = redisTemplate.opsForList().size(EMAIL_QUEUE_KEY);
            return size != null ? size : 0;
        } catch (Exception e) {
            logger.error("Failed to get queue size from Redis: {}", e.getMessage(), e);
            return 0;
        }
    }

    /**
     * Xử lý email queue - chạy mỗi 10 giây
     */
    @Scheduled(fixedDelay = 10000)
    public void processEmailQueue() {
        processNextEmail();
    }

    /**
     * Xử lý email tiếp theo trong queue
     */
    @Async("emailRedisTaskExecutor")
    public void processNextEmail() {
        try {
            logger.info("Processing next email from Redis queue...");

            // Sử dụng RPOP để lấy email từ cuối list (FIFO)
            EmailMessage emailMessage = redisTemplate.opsForList().rightPop(EMAIL_QUEUE_KEY);

            if (emailMessage == null) {
                logger.info("Redis email queue is empty.");
                return;
            }

            // Giả lập gửi email
            // Thực hiện gửi email ở đây (ví dụ: sử dụng JavaMailSender)
            try {
                Thread.sleep(1000); // Giả lập thời gian gửi email
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Email processing interrupted: {}", e.getMessage());
                return;
            }

            logger.info("🚀 Email sent successfully from Redis queue to: {}", emailMessage.getTo());
            logger.info("Remaining emails in Redis queue: {}", getQueueSize());

        } catch (Exception e) {
            logger.error("Error processing email from Redis queue: {}", e.getMessage(), e);
        }
    }
}
