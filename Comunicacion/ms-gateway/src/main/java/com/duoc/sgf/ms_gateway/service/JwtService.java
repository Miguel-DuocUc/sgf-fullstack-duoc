package com.duoc.sgf.ms_gateway.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    @Value("${global.operator.os.jwt.secret}")
    private String secret;

    private SecretKey key;

    @PostConstruct
    public void init() {

        byte[] keyBytes = secret.getBytes();

        key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);
            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }
}