package com.duoc.sgf.ms_auth.service;

import com.duoc.sgf.ms_auth.dto.LoginRequestDto;
import com.duoc.sgf.ms_auth.dto.TokenResponseDto;
import com.duoc.sgf.ms_auth.model.Usuario;
import com.duoc.sgf.ms_auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public TokenResponseDto login(LoginRequestDto request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generarToken(usuario);

        return new TokenResponseDto(token);
    }
}