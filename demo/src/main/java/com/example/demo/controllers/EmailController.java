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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/email")
@Tag(name = "Email Management", description = "APIs for managing email queue and sending emails")
@SecurityRequirement(name = "bearerAuth")
public class EmailController {
	private final EmailQueueService emailQueueService;

	public EmailController(EmailQueueService emailQueueService) {
		this.emailQueueService = emailQueueService;
	}

	@Operation(summary = "Send email", description = "Add an email to the processing queue for asynchronous delivery")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Email queued successfully", content = @Content(mediaType = "application/json", schema = @Schema(example = "{\n"
					+
					"  \"success\": true,\n" +
					"  \"message\": \"Email added to queue successfully\",\n" +
					"  \"queueSize\": 5\n" +
					"}"))),
			@ApiResponse(responseCode = "400", description = "Invalid email data", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT token", content = @Content)
	})
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
