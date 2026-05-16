package com.duoc.sgf.ms_identity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserBasicDto {

    private Long id;
    private String rut;
    private String name;
    private String lastName;
    private String email;
    private String role;
    private String status;
    private LocalDateTime createdAt;
}