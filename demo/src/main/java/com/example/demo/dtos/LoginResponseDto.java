package com.example.demo.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponseDto {
    String username;
    Long id;
    String accessToken;
}
