-- =============================================================================
-- EduSync - V3: student
-- Separate from app_user: domain-specific (student_number, grades).
-- Auth: Cognito. No password. No Azure AD OID.
-- Teams attendance matched via email.
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS student (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                 UUID           NOT NULL DEFAULT gen_random_uuid(),
    cognito_sub          VARCHAR(255)   NOT NULL,
    student_number       VARCHAR(50)    NOT NULL,
    first_name           VARCHAR(100)   NOT NULL,
    last_name            VARCHAR(100)   NOT NULL,
    email                VARCHAR(255)   NOT NULL,
    phone                VARCHAR(20),
    profile_image_s3_key VARCHAR(500),
    active               BOOLEAN        NOT NULL DEFAULT TRUE,
    blocked_at           TIMESTAMPTZ,
    blocked_by           BIGINT,
    blocked_reason       VARCHAR(500),
    last_login_at        TIMESTAMPTZ,
    created_at           TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_student_uuid        UNIQUE (uuid),
    CONSTRAINT uk_student_cognito_sub UNIQUE (cognito_sub),
    CONSTRAINT uk_student_number      UNIQUE (student_number),
    CONSTRAINT uk_student_email       UNIQUE (email),
    CONSTRAINT fk_student_blocked_by  FOREIGN KEY (blocked_by)
                                      REFERENCES app_user (id) ON DELETE SET NULL,
    CONSTRAINT chk_student_blocked    CHECK (
        (active = FALSE AND blocked_at IS NOT NULL AND blocked_reason IS NOT NULL)
        OR (active = TRUE AND blocked_at IS NULL AND blocked_by IS NULL AND blocked_reason IS NULL)
    )
);

COMMENT ON TABLE  student                      IS 'Students. Auth via Cognito. Teams attendance matched by email.';
COMMENT ON COLUMN student.profile_image_s3_key IS 'S3 key: profiles/{uuid}/avatar.jpg';
COMMENT ON COLUMN student.blocked_by           IS 'The admin (app_user) who blocked this student from the system.';
