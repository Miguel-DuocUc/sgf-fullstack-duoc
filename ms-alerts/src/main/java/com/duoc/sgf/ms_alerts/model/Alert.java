package com.duoc.sgf.ms_alerts.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pasaporte_ciudadano", nullable = false, length = 50)
    private String pasaporteCiudadano;

    @Column(name = "nombre_completo", length = 150)
    private String nombreCompleto;

    @Column(name = "tipo_alerta", nullable = false, length = 30)
    private String tipoAlerta;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "emitido_por", nullable = false, length = 100)
    private String emitidoPor;
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private boolean activa = true;
}