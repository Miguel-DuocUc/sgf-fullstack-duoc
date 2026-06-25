package com.duoc.sgf.ms_bordercontrol.model.dto;

import lombok.Data;

@Data
public class LogisticsCheckpointBasicDto {

    private Long id;
    private String name;
    private String direccion;
    private String estadoOperativo;
    private Integer cantPersonMax;
    private Integer guardPerson;
}