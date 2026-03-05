-- =============================================================================
-- EduSync - V15: assessment_submission (FIXED grading constraint)
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE submission_status AS ENUM ('SUBMITTED', 'GRADED', 'RETURNED', 'RESUBMITTED');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS assessment_submission (
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid             UUID              NOT NULL DEFAULT gen_random_uuid(),
    assessment_id    BIGINT            NOT NULL,
    student_id       BIGINT            NOT NULL,
    submission_text  TEXT,
    s3_key           VARCHAR(500),
    file_name        VARCHAR(255),
    file_size_bytes  BIGINT,
    mime_type        VARCHAR(100),
    submitted_at     TIMESTAMPTZ       NOT NULL DEFAULT NOW(),
    is_late          BOOLEAN           NOT NULL DEFAULT FALSE,
    marks_obtained   DECIMAL(6,2),
    feedback         TEXT,
    graded_by        BIGINT,
    graded_at        TIMESTAMPTZ,
    status           submission_status NOT NULL DEFAULT 'SUBMITTED',
    created_at       TIMESTAMPTZ       NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ       NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_submission_uuid           UNIQUE (uuid),
    CONSTRAINT uk_submission_assess_student UNIQUE (assessment_id, student_id),
    CONSTRAINT fk_submission_assessment     FOREIGN KEY (assessment_id)
                                            REFERENCES assessment (id) ON DELETE CASCADE,
    CONSTRAINT fk_submission_student        FOREIGN KEY (student_id)
                                            REFERENCES student (id) ON DELETE RESTRICT,
    CONSTRAINT fk_submission_graded_by      FOREIGN KEY (graded_by)
                                            REFERENCES app_user (id) ON DELETE SET NULL,
    CONSTRAINT chk_submission_has_content   CHECK (submission_text IS NOT NULL OR s3_key IS NOT NULL),
    CONSTRAINT chk_submission_marks         CHECK (marks_obtained IS NULL OR marks_obtained >= 0),
    CONSTRAINT chk_submission_graded        CHECK (
        (graded_by IS NOT NULL AND graded_at IS NOT NULL)
        OR (graded_by IS NULL AND graded_at IS NULL AND marks_obtained IS NULL)
    ),
    CONSTRAINT chk_submission_file_size     CHECK (file_size_bytes IS NULL OR file_size_bytes > 0)
);

COMMENT ON COLUMN assessment_submission.s3_key IS 'S3: submissions/{module_uuid}/{assessment_uuid}/{student_uuid}/{file}';
COMMENT ON CONSTRAINT chk_submission_graded ON assessment_submission IS 'Allows marks_obtained to be NULL during grading process. Final marks set by rubric trigger or manual entry.';
