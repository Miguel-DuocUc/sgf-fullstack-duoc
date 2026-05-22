package com.duoc.sgf.ms_auth.service;

import com.duoc.sgf.ms_auth.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${global.operator.os.jwt.secret}")
    private String secretKey;

    @Value("${global.operator.os.jwt.expiration}")
    private long jwtExpiration;

    public String generarToken(Usuario usuario) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", usuario.getRoles());
        claims.put("id", usuario.getId());

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(obtenerLlaveFirma())
                .compact();
    }

    public String extraerUsername(String token) {
        return extraerClaims(token).getSubject();
    }

    public boolean validarToken(String token, UserDetails userDetails) {

        final String username = extraerUsername(token);

        return username.equals(userDetails.getUsername())
                && !tokenExpirado(token);
    }

    private boolean tokenExpirado(String token) {

        return extraerClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extraerClaims(String token) {

        return Jwts.parser()
                .verifyWith(obtenerLlaveFirma())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey obtenerLlaveFirma() {

        byte[] keyBytes = secretKey.getBytes();

        return Keys.hmacShaKeyFor(keyBytes);
    }
}