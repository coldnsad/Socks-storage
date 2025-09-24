package org.example.socks.dto.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(
        String message,
        HttpStatus status
) { }
