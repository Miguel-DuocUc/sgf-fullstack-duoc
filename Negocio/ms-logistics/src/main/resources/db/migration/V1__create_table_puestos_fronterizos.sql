CREATE SEQUENCE SEQ_PUESTOS_FRONTERIZOS START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;


CREATE TABLE puestos_fronterizos (
                                     id NUMBER(19,0) NOT NULL,
                                     nombre_puesto_fronterizo VARCHAR2(255 CHAR) NOT NULL,
                                     direccion VARCHAR2(255 CHAR) NOT NULL,
                                     estado_operativo VARCHAR2(255 CHAR) NOT NULL,
                                     cant_person_max NUMBER(10,0) NOT NULL,
                                     guardias_person NUMBER(10,0) NOT NULL,
                                     CONSTRAINT pk_puestos_fronterizos PRIMARY KEY (id)
);