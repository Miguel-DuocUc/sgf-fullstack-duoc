package com.duoc.sgf.ms_auth.service;

import com.duoc.sgf.ms_auth.dto.LoginRequestDto;
import com.duoc.sgf.ms_auth.dto.TokenResponseDto;
import com.duoc.sgf.ms_auth.model.Usuario;
import com.duoc.sgf.ms_auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public TokenResponseDto login(LoginRequestDto request) {
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas (Usuario no encontrado)"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales incorrectas (Contraseña inválida)");
        }

        String token = jwtService.generarToken(usuario);

        return new TokenResponseDto(token);
    }
}