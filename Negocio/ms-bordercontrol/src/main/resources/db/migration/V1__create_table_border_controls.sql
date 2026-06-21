CREATE SEQUENCE SEQ_BORDER_CONTROLS START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE BORDER_CONTROLS (
                                 id NUMBER(19,0) NOT NULL,
                                 user_id NUMBER(19,0) NOT NULL,
                                 identity_document_id NUMBER(19,0) NOT NULL,
                                 visa_request_id NUMBER(19,0) NOT NULL,
                                 health_declaration_id NUMBER(19,0) NOT NULL,
                                 logistics_checkpoint_id NUMBER(19,0) NOT NULL,
                                 officer_name VARCHAR2(100 CHAR) NOT NULL,
                                 movement_type VARCHAR2(30 CHAR) NOT NULL,
                                 status VARCHAR2(30 CHAR) NOT NULL,
                                 observations VARCHAR2(250 CHAR),
                                 created_at TIMESTAMP NOT NULL,
                                 CONSTRAINT pk_border_controls PRIMARY KEY (id)
);