package com.example.demo.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.LoginRequestDto;
import com.example.demo.dtos.LoginResponseDto;
import com.example.demo.services.SqlInjectionService;

@RestController()
@RequestMapping("/api/sql-injection")
public class SqlInjectionController {

	private final SqlInjectionService sqlInjectionService;

	public SqlInjectionController(SqlInjectionService sqlInjectionService) {
		this.sqlInjectionService = sqlInjectionService;
	}

	@PostMapping("/login")
	public LoginResponseDto login(@RequestBody LoginRequestDto request) {
		return sqlInjectionService.login(request);
	}
}
