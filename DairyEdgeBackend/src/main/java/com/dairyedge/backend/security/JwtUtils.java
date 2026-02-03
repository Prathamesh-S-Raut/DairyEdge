package com.dairyedge.backend.security;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.time}")
    private long jwtExpirationTime;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        log.info("Initializing secret key for JWT");
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(UserPrincipal principal) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationTime);

        return Jwts.builder()
                .subject(principal.getEmail())
                .issuedAt(now)
                .expiration(expiry)
                .claims(Map.of(
                        "user_id", principal.getUserId(),
                        "user_role", principal.getUserRole()
                ))
                .signWith(secretKey)
                .compact();
    }

    public Claims validateToken(String jwt) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
