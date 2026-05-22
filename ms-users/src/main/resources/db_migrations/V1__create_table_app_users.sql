CREATE SEQUENCE SEQ_APP_USERS START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE APP_USERS (
                           id NUMBER(19,0) NOT NULL,
                           rut VARCHAR2(12 CHAR) NOT NULL,
                           name VARCHAR2(80 CHAR) NOT NULL,
                           last_name VARCHAR2(80 CHAR) NOT NULL,
                           email VARCHAR2(120 CHAR) NOT NULL,
                           password VARCHAR2(255 CHAR) NOT NULL,
                           role VARCHAR2(30 CHAR) NOT NULL,
                           status VARCHAR2(30 CHAR) NOT NULL,
                           created_at TIMESTAMP NOT NULL,
                           CONSTRAINT pk_app_users PRIMARY KEY (id),
                           CONSTRAINT uk_app_users_rut UNIQUE (rut),
                           CONSTRAINT uk_app_users_email UNIQUE (email)
);