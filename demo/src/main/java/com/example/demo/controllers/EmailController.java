package com.example.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.EmailMessage;
import com.example.demo.services.EmailQueueService;

@RestController
@RequestMapping("/api/email")
public class EmailController {
	private final EmailQueueService emailQueueService;

	public EmailController(EmailQueueService emailQueueService) {
		this.emailQueueService = emailQueueService;
	}

	@PostMapping("/send")
	public ResponseEntity<Map<String, Object>> sendEmail(@RequestBody EmailMessage emailMessage) {
		Map<String, Object> response = new HashMap<>();

		boolean queued = emailQueueService.addToQueue(emailMessage);

		if (queued) {
			response.put("success", true);
			response.put("message", "Email added to queue successfully");
			response.put("queueSize", emailQueueService.getQueueSize());
		} else {
			response.put("success", false);
			response.put("message", "Failed to add email to queue");
		}

		return ResponseEntity.ok(response);
	}
}
