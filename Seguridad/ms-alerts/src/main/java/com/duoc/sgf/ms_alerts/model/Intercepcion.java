package com.duoc.sgf.ms_alerts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "intercepciones_alerta")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Intercepcion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "intercepcion_seq")
    @SequenceGenerator(name = "intercepcion_seq", sequenceName = "SEQ_INTERCEPCIONES", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alerta_id", nullable = false)
    private Alert alerta;

    @Column(name = "pasaporte_ciudadano", nullable = false, length = 50)
    private String pasaporteCiudadano;

    @Column(name = "fecha_intercepcion", nullable = false)
    private LocalDateTime fechaIntercepcion;
}