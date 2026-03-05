-- =============================================================================
-- EduSync - V30: certificate
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE certificate_status AS ENUM ('PENDING', 'ISSUED', 'REVOKED');
EXCEPTION WHEN duplicate_object THEN
    -- Safe to drop and recreate: no table uses certificate_status before V30.
    IF NOT EXISTS (
        SELECT 1 FROM pg_enum e JOIN pg_type t ON e.enumtypid = t.oid
        WHERE t.typname = 'certificate_status' AND e.enumlabel = 'ISSUED'
    ) THEN
        DROP TYPE certificate_status;
        CREATE TYPE certificate_status AS ENUM ('PENDING', 'ISSUED', 'REVOKED');
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS certificate (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                 UUID               NOT NULL DEFAULT gen_random_uuid(),
    course_id            BIGINT             NOT NULL,
    student_id           BIGINT             NOT NULL,
    enrollment_id        BIGINT             NOT NULL,
    issued_by            BIGINT             NOT NULL,
    certificate_number   VARCHAR(50)        NOT NULL,
    verification_code    VARCHAR(100)       NOT NULL,
    final_grade          DECIMAL(5,2),
    final_attendance_pct DECIMAL(5,2),
    completion_date      DATE               NOT NULL,
    s3_key               VARCHAR(500),
    status               certificate_status NOT NULL DEFAULT 'PENDING',
    issued_at            TIMESTAMPTZ,
    revoked_at           TIMESTAMPTZ,
    revocation_reason    VARCHAR(500),
    created_at           TIMESTAMPTZ        NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMPTZ        NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_certificate_uuid       UNIQUE (uuid),
    CONSTRAINT uk_certificate_number     UNIQUE (certificate_number),
    CONSTRAINT uk_certificate_verify     UNIQUE (verification_code),
    CONSTRAINT uk_certificate_enrollment UNIQUE (enrollment_id),
    CONSTRAINT fk_cert_course            FOREIGN KEY (course_id)
                                         REFERENCES course (id) ON DELETE RESTRICT,
    CONSTRAINT fk_cert_student           FOREIGN KEY (student_id)
                                         REFERENCES student (id) ON DELETE RESTRICT,
    CONSTRAINT fk_cert_enrollment        FOREIGN KEY (enrollment_id)
                                         REFERENCES course_enrollment (id) ON DELETE RESTRICT,
    CONSTRAINT fk_cert_issued_by         FOREIGN KEY (issued_by)
                                         REFERENCES app_user (id) ON DELETE RESTRICT,
    CONSTRAINT chk_cert_grade            CHECK (final_grade IS NULL OR final_grade BETWEEN 0 AND 100),
    CONSTRAINT chk_cert_attendance       CHECK (final_attendance_pct IS NULL
                                                OR final_attendance_pct BETWEEN 0 AND 100),
    CONSTRAINT chk_cert_issued           CHECK (
        (status = 'ISSUED' AND issued_at IS NOT NULL) OR (status != 'ISSUED')
    ),
    CONSTRAINT chk_cert_revoked          CHECK (
        (status = 'REVOKED' AND revoked_at IS NOT NULL AND revocation_reason IS NOT NULL)
        OR (status != 'REVOKED')
    )
);

COMMENT ON COLUMN certificate.s3_key IS 'S3: certificates/{course_uuid}/{student_uuid}/{cert_number}.pdf';
