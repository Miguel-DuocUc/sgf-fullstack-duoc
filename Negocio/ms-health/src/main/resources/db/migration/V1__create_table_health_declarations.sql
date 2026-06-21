
CREATE SEQUENCE SEQ_HEALTH_DECLARATIONS START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE health_declarations (
                                     id NUMBER(19,0) NOT NULL,
                                     user_id NUMBER(19,0) NOT NULL,
                                     identity_document_id NUMBER(19,0) NOT NULL,
                                     has_symptoms BOOLEAN NOT NULL,
                                     symptoms_description VARCHAR2(250 CHAR),
                                     has_recent_contact BOOLEAN NOT NULL,
                                     vaccination_status VARCHAR2(50 CHAR) NOT NULL,
                                     risk_level VARCHAR2(30 CHAR) NOT NULL,
                                     status VARCHAR2(30 CHAR) NOT NULL,
                                     observations VARCHAR2(250 CHAR),
                                     created_at TIMESTAMP NOT NULL,
                                     CONSTRAINT pk_health_declarations PRIMARY KEY (id)
);