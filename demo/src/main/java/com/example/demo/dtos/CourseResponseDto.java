package com.example.demo.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "DTO for course response")
@Getter
@Setter
public class CourseResponseDto {

	@Schema(description = "Unique identifier of the course", example = "1")
	private Long id;

	@Schema(description = "Name of the course", example = "Introduction to Programming")
	private String name;
}
