package com.duoc.sgf.ms_logistics.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

@Entity
@Table(name = "puestos_fronterizos")

public class PuestoFronterizo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nombre_puesto_fronterizo")
    private String name;

    @NotBlank
    @Column(name = "direccion")
    private String direccion;

    @NotBlank
    @Column(name = "estado_operativo")
    private String estadoOperativo;

    @NotNull
    @Positive
    @Column(name = "cant_person_max")
    private Integer cantPersonMax;
    //
    @NotNull
    @Positive
    @Column(name = "guardias_person")
    private Integer guardPerson;

}
