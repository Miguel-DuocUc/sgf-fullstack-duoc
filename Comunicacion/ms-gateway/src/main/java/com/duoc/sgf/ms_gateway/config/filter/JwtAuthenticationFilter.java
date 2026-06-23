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

        // 1. Extraemos la cabecera "Authorization" de la petición HTTP reactiva
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // 2. Si no hay token o no empieza con "Bearer ", dejamos pasar la petición
        // (Spring Security la bloqueará después si la ruta no es permitida)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return chain.filter(exchange);
        }

        // 3. Extraemos el token limpio
        String token = authHeader.substring(7);

        try {
            // 4. Validamos el token (ajusta los nombres de estos métodos según tu JwtService)
            String username = jwtService.extractUsername(token);

            if (username != null && jwtService.isTokenValid(token)) {

                // 5. Creamos el objeto de autenticación
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        Collections.emptyList() // Aquí irían los roles si los manejas
                );

                // 6. ¡LA MAGIA REACTIVA! Inyectamos la autenticación en el Contexto de WebFlux
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
        } catch (Exception e) {
            // Si el token es inválido, expirado, o está mal formado,
            // simplemente no lo autenticamos y Spring devolverá un 401 Unauthorized
            System.out.println("Error validando token JWT en Gateway: " + e.getMessage());
        }

        return chain.filter(exchange);
    }
}