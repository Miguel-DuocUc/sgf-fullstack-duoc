CREATE TABLE alertas (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         pasaporte_ciudadano VARCHAR(50) NOT NULL,
                         nombre_completo VARCHAR(150),
                         tipo_alerta VARCHAR(30) NOT NULL,
                         motivo TEXT NOT NULL,
                         emitido_por VARCHAR(100) NOT NULL,
                         fecha_creacion DATETIME NOT NULL,
                         activa BOOLEAN NOT NULL DEFAULT TRUE
);