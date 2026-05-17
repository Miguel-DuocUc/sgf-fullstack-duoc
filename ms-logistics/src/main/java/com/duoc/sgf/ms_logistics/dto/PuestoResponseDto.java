package com.duoc.sgf.ms_logistics.dto;

import lombok.Data;

@Data
public class PuestoResponseDto {

    private Long id;
    private String name ;
    private String direccion;
    private String estadoOperativo;
    private Integer cantPersonMax;
    private Integer guardPerson;

}

