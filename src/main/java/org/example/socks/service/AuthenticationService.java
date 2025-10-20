package org.example.socks.service;

import org.example.socks.dto.auth.LoginDto;
import org.example.socks.dto.auth.LoginResponseDto;
import org.example.socks.dto.auth.RegisterDto;

public interface AuthenticationService {

    void signUp(RegisterDto registerDto);

    LoginResponseDto login(LoginDto loginDto);
}
