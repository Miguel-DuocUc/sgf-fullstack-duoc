package com.duoc.sgf.ms_logistics.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos requeridos para registrar o actualizar un puesto de control logístico fronterizo.")
public class PuestoRequestDto {

    @NotBlank(message = "El nombre del puesto fronterizo es obligatorio")
    @Schema(description = "Nombre oficial del complejo o puesto fronterizo", example = "Paso Los Libertadores")
    private String name;

    @NotBlank(message = "La dirección es obligatoria")
    @Schema(description = "Ubicación geográfica o ruta del recinto", example = "Ruta Internacional 60, Región de Valparaíso")
    private String direccion;

    @NotNull(message = "La capacidad máxima de personas es obligatoria")
    @Positive(message = "La capacidad máxima debe ser un número positivo")
    @Schema(description = "Aforo máximo de ciudadanos y vehículos permitidos simultáneamente", example = "1500")
    private Integer cantPersonMax;

    @NotNull(message = "La cantidad de guardias es obligatoria")
    @Positive(message = "La cantidad de guardias debe ser un número positivo")
    @Schema(description = "Número de agentes de seguridad y personal operativo asignados", example = "45")
    private Integer guardPerson;

    @NotBlank(message = "El estado operativo es obligatorio")
    @Schema(description = "Estado logístico actual del recinto", example = "OPERATIVO")
    private String estadoOperativo;

}