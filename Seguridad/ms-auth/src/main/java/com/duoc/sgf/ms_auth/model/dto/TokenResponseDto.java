package com.duoc.sgf.ms_auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponseDto {
    private String token;
    private String tipo = "Bearer";

    public TokenResponseDto(String token) {
        this.token = token;
    }
}