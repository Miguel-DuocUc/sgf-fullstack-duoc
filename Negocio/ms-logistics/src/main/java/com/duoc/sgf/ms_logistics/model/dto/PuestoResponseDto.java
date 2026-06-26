package com.duoc.sgf.ms_logistics.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Objeto de respuesta con los datos logísticos consolidados de un puesto fronterizo.")
public class PuestoResponseDto {

    @Schema(description = "Identificador único interno del puesto logístico", example = "3")
    private Long id;

    @Schema(description = "Nombre oficial del puesto fronterizo", example = "Paso Los Libertadores")
    private String name;

    @Schema(description = "Ubicación geográfica o ruta", example = "Ruta Internacional 60, Región de Valparaíso")
    private String direccion;

    @Schema(description = "Estado logístico actual", example = "OPERATIVO")
    private String estadoOperativo;

    @Schema(description = "Aforo máximo del recinto", example = "1500")
    private Integer cantPersonMax;

    @Schema(description = "Personal de seguridad desplegado", example = "45")
    private Integer guardPerson;

}