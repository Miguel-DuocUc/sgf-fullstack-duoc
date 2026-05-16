package com.duoc.sgf.ms_logistics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuestoRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String direccion;

    @NotNull
    private Integer cantPersonMax;

    @NotNull
    private Integer guardPerson;
}
