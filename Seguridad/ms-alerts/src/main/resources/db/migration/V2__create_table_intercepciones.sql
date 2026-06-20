CREATE SEQUENCE SEQ_INTERCEPCIONES START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE intercepciones_alerta (
                                       id NUMBER(19,0) NOT NULL,
                                       alerta_id NUMBER(19,0) NOT NULL,
                                       pasaporte_ciudadano VARCHAR2(50 CHAR) NOT NULL,
                                       fecha_intercepcion TIMESTAMP NOT NULL,
                                       CONSTRAINT pk_intercepciones PRIMARY KEY (id),
                                       CONSTRAINT fk_intercepcion_alerta FOREIGN KEY (alerta_id) REFERENCES alertas(id)
);