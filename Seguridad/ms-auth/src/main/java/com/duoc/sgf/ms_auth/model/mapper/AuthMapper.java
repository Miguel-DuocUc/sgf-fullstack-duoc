package com.duoc.sgf.ms_auth.model.mapper;

import com.duoc.sgf.ms_auth.model.Usuario;
import com.duoc.sgf.ms_auth.model.dto.LoginRequestDto;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public Usuario toEntity(LoginRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Usuario entity = new Usuario();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());

        return entity;
    }

}