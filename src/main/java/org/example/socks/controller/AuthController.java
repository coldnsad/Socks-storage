package org.example.socks.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.socks.dto.auth.LoginDto;
import org.example.socks.dto.auth.LoginResponseDto;
import org.example.socks.dto.auth.RegisterDto;
import org.example.socks.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public String signUp(@RequestBody @Valid RegisterDto registerDto) {
        authService.signUp(registerDto);
        return "success";
    }

    @GetMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginDto loginDto) {
        LoginResponseDto loginResponse = authService.login(loginDto);
        return ResponseEntity.ok(loginResponse);
    }
}
