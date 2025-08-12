package com.example.demo.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class EmailMessage {
	private String to;
	private String subject;
	private String body;
}
