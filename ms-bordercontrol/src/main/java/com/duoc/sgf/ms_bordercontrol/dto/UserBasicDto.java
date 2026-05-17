package com.duoc.sgf.ms_bordercontrol.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
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