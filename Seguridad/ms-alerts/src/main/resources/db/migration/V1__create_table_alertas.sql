CREATE SEQUENCE SEQ_ALERTAS START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE alertas (
                         id NUMBER(19,0) NOT NULL,
                         pasaporte_ciudadano VARCHAR2(50 CHAR) NOT NULL,
                         nombre_completo VARCHAR2(150 CHAR),
                         tipo_alerta VARCHAR2(30 CHAR) NOT NULL,
                         motivo VARCHAR2(4000 CHAR) NOT NULL,
                         emitido_por VARCHAR2(100 CHAR) NOT NULL,
                         fecha_creacion TIMESTAMP NOT NULL,
                         activa BOOLEAN NOT NULL,
                         CONSTRAINT pk_alertas PRIMARY KEY (id)
);