package com.duoc.sgf.ms_users.model.mapper;

import com.duoc.sgf.ms_users.model.User;
import com.duoc.sgf.ms_users.model.dto.UserRequestDto;
import com.duoc.sgf.ms_users.model.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto dto) {
        User entity = new User();
        entity.setRut(dto.getRut());
        entity.setName(dto.getName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setRole(dto.getRole());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public UserResponseDto toDto(User entity) {
        return new UserResponseDto(
                entity.getId(),
                entity.getRut(),
                entity.getName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getRole(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}