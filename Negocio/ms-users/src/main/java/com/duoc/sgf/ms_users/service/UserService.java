package com.duoc.sgf.ms_users.service;

import com.duoc.sgf.ms_users.model.dto.UserRequestDto;
import com.duoc.sgf.ms_users.model.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    List<UserResponseDto> findAll();

    UserResponseDto findById(Long id);

    UserResponseDto findByRut(String rut);

    List<UserResponseDto> findByRole(String role);

    UserResponseDto create(UserRequestDto request);

    UserResponseDto update(Long id, UserRequestDto request);

    void delete(Long id);
}