package com.duoc.sgf.ms_logistics.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Agregado: Muy útil para crear objetos limpios en los Mappers/Servicios
@Entity
@Table(name = "puestos_fronterizos")
public class PuestoFronterizo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "puesto_fronterizo_seq")
    @SequenceGenerator(name = "puesto_fronterizo_seq", sequenceName = "SEQ_PUESTOS_FRONTERIZOS", allocationSize = 1)
    private Long id;

    @NotBlank(message = "El nombre del puesto fronterizo es obligatorio")
    @Column(name = "nombre_puesto_fronterizo", nullable = false, length = 255)
    private String name;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @NotBlank(message = "El estado operativo es obligatorio")
    @Column(name = "estado_operativo", nullable = false, length = 255)
    private String estadoOperativo;

    @NotNull(message = "La capacidad máxima de personas es obligatoria")
    @Positive(message = "La capacidad máxima debe ser un número positivo")
    @Column(name = "cant_person_max", nullable = false)
    private Integer cantPersonMax;

    @NotNull(message = "La cantidad de guardias es obligatoria")
    @Positive(message = "La cantidad de guardias debe ser un número positivo")
    @Column(name = "guardias_person", nullable = false)
    private Integer guardPerson;

}
