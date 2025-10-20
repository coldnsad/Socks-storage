package org.example.socks.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @Email
        String email,
        @NotNull
        String password
) {
}
