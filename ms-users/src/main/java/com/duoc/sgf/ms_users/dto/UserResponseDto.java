package com.duoc.sgf.ms_users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String rut;
    private String name;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private LocalDateTime createdAt;
}