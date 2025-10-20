package org.example.socks.service;

import io.jsonwebtoken.Claims;
import org.example.socks.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Set;

public interface JwtService {
    String generateToken(UserDetails userDetails);

    boolean isTokenValid(String token);

    String extractUsername(String token);

    boolean isTokenExpired(String token);

    Key getSignKey();

    Claims extractAllClaims(String token);

    LocalDateTime getExpirationDate(String token);

    Set<Role> getRoles(String token);

    Authentication getAuthentication(String token);

}
