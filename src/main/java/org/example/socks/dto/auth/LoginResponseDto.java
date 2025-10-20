package org.example.socks.dto.auth;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoginResponseDto(
        String token,
        LocalDateTime expirationDate
) {
}
