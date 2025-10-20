package org.example.socks.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.example.socks.model.Role;

import java.util.Set;

public record RegisterDto(
        @Email(message = "Email is not valid")
        String email,
        @NotNull
        String password,
        @NotNull
        Set<Role> roles
) {
}
