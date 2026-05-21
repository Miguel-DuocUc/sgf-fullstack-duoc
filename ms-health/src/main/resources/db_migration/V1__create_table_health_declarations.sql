CREATE TABLE IF NOT EXISTS health_declarations (
                                                   id BIGINT NOT NULL AUTO_INCREMENT,
                                                   user_id BIGINT NOT NULL,
                                                   identity_document_id BIGINT NOT NULL,
                                                   has_symptoms BOOLEAN NOT NULL,
                                                   symptoms_description VARCHAR(250),
    has_recent_contact BOOLEAN NOT NULL,
    vaccination_status VARCHAR(50) NOT NULL,
    risk_level VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    observations VARCHAR(250),
    created_at DATETIME NOT NULL,
    CONSTRAINT pk_health_declarations PRIMARY KEY (id)
    );