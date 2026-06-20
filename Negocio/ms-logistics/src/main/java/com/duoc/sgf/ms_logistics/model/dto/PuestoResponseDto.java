package com.duoc.sgf.ms_logistics.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PuestoResponseDto {

    private Long id;
    private String name;
    private String direccion;
    private String estadoOperativo;
    private Integer cantPersonMax;
    private Integer guardPerson;

}