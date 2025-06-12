package com.springsecurity.service;

import com.springsecurity.model.User;
import com.springsecurity.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class JWTService {

    private final UserRepository userRepository;
    private static final String SECRET_KEY =
            "4B6250655368566D5970337336763979244226452948404D635166546A576E5A"; // 256-bit key (32 bytes = 64 hex chars)

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException("User with username: "+username+" not found"));
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateToken(String token, String username) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith((SecretKey) getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String tokenUsername = claims.getSubject();
            Date expiration = claims.getExpiration();

            return (tokenUsername.equals(username) && !expiration.before(new Date()));
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String role = claims.get("role", String.class); // Extract single role

        return List.of(new SimpleGrantedAuthority("ROLE_" + role)); // Spring expects "ROLE_..." format
    }







}
