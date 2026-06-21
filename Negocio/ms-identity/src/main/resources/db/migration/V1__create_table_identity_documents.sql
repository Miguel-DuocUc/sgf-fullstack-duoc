CREATE SEQUENCE SEQ_IDENTITY_DOCUMENTS START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE IDENTITY_DOCUMENTS (
                                    id NUMBER(19,0) NOT NULL,
                                    user_id NUMBER(19,0) NOT NULL,
                                    document_type VARCHAR2(50 CHAR) NOT NULL,
                                    document_number VARCHAR2(50 CHAR) NOT NULL,
                                    issuing_country VARCHAR2(80 CHAR) NOT NULL,
                                    holder_name VARCHAR2(100 CHAR) NOT NULL,
                                    holder_last_name VARCHAR2(100 CHAR) NOT NULL,
                                    expiration_date DATE NOT NULL,
                                    minor BOOLEAN NOT NULL,
                                    notarized_authorization BOOLEAN NOT NULL,
                                    status VARCHAR2(30 CHAR) NOT NULL,
                                    created_at TIMESTAMP NOT NULL,
                                    CONSTRAINT pk_identity_documents PRIMARY KEY (id),
                                    CONSTRAINT uk_identity_document_number UNIQUE (document_number)

);