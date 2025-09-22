package com.example.demo.dtos;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO for paginated student response")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedStudentResponseDto {

    @Schema(description = "List of students in the current page")
    private List<StudentResponseDto> data;

    @Schema(description = "Current page number (1-based)", example = "1")
    private int pageNumber;

    @Schema(description = "Number of items per page", example = "5")
    private int pageSize;

    @Schema(description = "Total number of records available", example = "25")
    private long totalRecords;

    @Schema(description = "Total number of pages available", example = "5")
    private int totalPages;

    @Schema(description = "Whether there is a next page available", example = "true")
    private boolean hasNext;

    @Schema(description = "Whether there is a previous page available", example = "false")
    private boolean hasPrevious;
}