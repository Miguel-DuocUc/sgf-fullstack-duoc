CREATE SEQUENCE SEQ_VISA_REQUESTS START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE VISA_REQUESTS (
                               id NUMBER(19,0) NOT NULL,
                               user_id NUMBER(19,0) NOT NULL,
                               identity_document_id NUMBER(19,0) NOT NULL,
                               visa_type VARCHAR2(50 CHAR) NOT NULL,
                               destination_country VARCHAR2(80 CHAR) NOT NULL,
                               travel_purpose VARCHAR2(150 CHAR) NOT NULL,
                               start_date DATE NOT NULL,
                               end_date DATE NOT NULL,
                               status VARCHAR2(30 CHAR) NOT NULL,
                               observations VARCHAR2(250 CHAR),
                               created_at TIMESTAMP NOT NULL,
                               CONSTRAINT pk_visa_requests PRIMARY KEY (id)
);