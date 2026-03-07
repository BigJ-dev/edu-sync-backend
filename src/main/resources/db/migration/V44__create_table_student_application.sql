CREATE TABLE edusync.student_application
(
    id               BIGSERIAL PRIMARY KEY,
    uuid             UUID         NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    first_name       VARCHAR(100) NOT NULL,
    last_name        VARCHAR(100) NOT NULL,
    email            VARCHAR(255) NOT NULL UNIQUE,
    phone            VARCHAR(20),
    id_number        VARCHAR(20) UNIQUE,
    date_of_birth    DATE,
    address          TEXT,
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    rejection_reason VARCHAR(500),
    reviewed_by_id   BIGINT REFERENCES edusync.app_user (id),
    reviewed_at      TIMESTAMPTZ,
    student_id       BIGINT REFERENCES edusync.student (id),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE edusync.application_document
(
    id             BIGSERIAL PRIMARY KEY,
    uuid           UUID         NOT NULL DEFAULT gen_random_uuid() UNIQUE,
    application_id BIGINT       NOT NULL REFERENCES edusync.student_application (id) ON DELETE CASCADE,
    document_type  VARCHAR(50)  NOT NULL,
    document_name  VARCHAR(255) NOT NULL,
    s3_key         VARCHAR(500) NOT NULL,
    file_size_bytes BIGINT,
    mime_type      VARCHAR(100),
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_application_status ON edusync.student_application (status);
CREATE INDEX idx_application_email ON edusync.student_application (email);
CREATE INDEX idx_application_document_app_id ON edusync.application_document (application_id);
