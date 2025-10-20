package org.example.socks.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.example.socks.model.Role;
import org.example.socks.service.JwtService;
import org.example.socks.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.token}")
    private String secretKey;
    @Value("${jwt.expiration}")
    private Long expiration;
    private final UserService userService;

    @Override
    public String generateToken(UserDetails userDetails) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>(userDetails.getAuthorities());
        String username = userDetails.getUsername();

        Claims claims = Jwts.claims()
                .setSubject(username);
        claims.put("roles", grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token) {
        UserDetails userDetails = userService.loadUserByUsername(extractUsername(token));
        Set<Role> roles = getRoles(token);
        Set<Role> authorities = userDetails.getAuthorities()
                .stream()
                .map(authority -> {
                    String auth = authority.getAuthority();
                    return Role.valueOf(auth);
                })
                .collect(Collectors.toSet());
        return roles.equals(authorities);
    }

    @Override
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    @Override
    public Key getSignKey() {
        if (secretKey == null || secretKey.length() < 32) {
            throw new RuntimeException("Secret key is too short");
        }
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public LocalDateTime getExpirationDate(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @Override
    public Set<Role> getRoles(String token) {
        List<String> roles = extractAllClaims(token).get("roles", List.class);
        return roles.stream().map(Role::valueOf).collect(Collectors.toSet());
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userService.loadUserByUsername(extractUsername(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                null,
                userDetails.getAuthorities()
        );
    }

}
