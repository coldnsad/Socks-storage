package org.example.socks.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socks.dto.auth.LoginDto;
import org.example.socks.dto.auth.LoginResponseDto;
import org.example.socks.dto.auth.RegisterDto;
import org.example.socks.model.CustomUserDetails;
import org.example.socks.model.User;
import org.example.socks.repository.UserRepository;
import org.example.socks.service.AuthenticationService;
import org.example.socks.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public void signUp(RegisterDto registerDto) {
        User user = User.builder()
                .email(registerDto.email())
                .password(passwordEncoder.encode(registerDto.password()))
                .roles(registerDto.roles())
                .build();
        log.info("signUp User:{}", user);
        userRepository.save(user);
    }

    @Override
    public LoginResponseDto login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new UsernameNotFoundException("The email or password is incorrect"));
        if (!passwordEncoder.matches(loginDto.password(), user.getPassword())) {
            throw new UsernameNotFoundException("The email or password is incorrect");
        }
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.email(), loginDto.password())
        );
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        log.info("login User:{}", userDetails);
        String token = jwtService.generateToken(userDetails);
        log.info("login Token:{}", token);
        return LoginResponseDto.builder()
                .token(token)
                .expirationDate(jwtService.getExpirationDate(token))
                .build();
    }
}
