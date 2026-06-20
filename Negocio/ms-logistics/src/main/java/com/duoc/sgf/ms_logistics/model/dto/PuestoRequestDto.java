package com.duoc.sgf.ms_logistics.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuestoRequestDto {

    @NotBlank(message = "El nombre del puesto fronterizo es obligatorio")
    private String name;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "La capacidad máxima de personas es obligatoria")
    @Positive(message = "La capacidad máxima debe ser un número positivo")
    private Integer cantPersonMax;

    @NotNull(message = "La cantidad de guardias es obligatoria")
    @Positive(message = "La cantidad de guardias debe ser un número positivo")
    private Integer guardPerson;

    @NotBlank(message = "El estado operativo es obligatorio")
    private String estadoOperativo;

}