package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // Enable simple message broker to send messages to clients subscribed to
    // destinations prefixed with "/topic"
    config.enableSimpleBroker("/topic", "/queue");

    // Define prefix for messages bound for @MessageMapping methods
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // Register STOMP endpoints for websocket connections
    registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*")
        .withSockJS(); // Enable SockJS fallback options
  }
}
