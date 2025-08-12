package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.EmailMessage;
import com.example.demo.services.EmailRedisQueueService;

@RestController
@RequestMapping("/api/email-redis")
public class EmailRedisController {

  private final EmailRedisQueueService emailRedisQueueService;

  public EmailRedisController(EmailRedisQueueService emailRedisQueueService) {
    this.emailRedisQueueService = emailRedisQueueService;
  }

  @PostMapping("/send")
  public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody EmailMessage emailMessage) {
    Map<String, Object> response = new HashMap<>();

    boolean queued = emailRedisQueueService.addToQueue(emailMessage);

    if (queued) {
      response.put("success", true);
      response.put("message", "Email added to Redis queue successfully");
      response.put("queueSize", emailRedisQueueService.getQueueSize());
    } else {
      response.put("success", false);
      response.put("message", "Failed to add email to Redis queue");
    }

    return ResponseEntity.ok(response);
  }
}
