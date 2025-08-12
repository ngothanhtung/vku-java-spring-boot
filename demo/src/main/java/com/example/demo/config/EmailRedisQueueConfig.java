package com.example.demo.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.example.demo.models.EmailMessage;

@Configuration
@EnableAsync
@EnableScheduling
public class EmailRedisQueueConfig {

	@Bean("emailRedisTemplate")
	public RedisTemplate<String, EmailMessage> emailRedisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, EmailMessage> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// Sử dụng StringRedisSerializer cho key
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());

		// Sử dụng Jackson serializer cho value
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

		template.afterPropertiesSet();
		return template;
	}

	@Bean("emailRedisTaskExecutor")
	public Executor emailRedisTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(5);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("email-redis-sender-");
		executor.initialize();
		return executor;
	}
}
