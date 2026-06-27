package com.duoc.sgf.ms_gateway.config.filter;

import com.duoc.sgf.ms_gateway.service.JwtService; // Tu servicio para leer el token
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(token);

            if (username != null && jwtService.isTokenValid(token)) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList()
                );

                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
        } catch (Exception e) {
            System.out.println("Error validando token JWT en Gateway: " + e.getMessage());
        }

        return chain.filter(exchange);
    }
}