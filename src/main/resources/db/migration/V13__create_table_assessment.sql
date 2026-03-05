-- =============================================================================
-- EduSync - V13: assessment
-- Belongs to a MODULE. Manually graded work.
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE assessment_type AS ENUM ('ASSIGNMENT', 'PROJECT', 'PRACTICAL', 'PRESENTATION', 'OTHER');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE delivery_mode AS ENUM ('ONLINE', 'PHYSICAL');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE assessment_status AS ENUM ('DRAFT', 'PUBLISHED', 'CLOSED', 'GRADED');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS assessment (
    id                     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                   UUID              NOT NULL DEFAULT gen_random_uuid(),
    module_id              BIGINT            NOT NULL,
    created_by             BIGINT            NOT NULL,
    title                  VARCHAR(255)      NOT NULL,
    description            TEXT,
    assessment_type        assessment_type   NOT NULL,
    delivery_mode          delivery_mode     NOT NULL DEFAULT 'ONLINE',
    total_marks            DECIMAL(6,2)      NOT NULL,
    weight_pct             DECIMAL(5,2),
    due_date               TIMESTAMPTZ       NOT NULL,
    visible_from           TIMESTAMPTZ,
    allow_late_submission  BOOLEAN           NOT NULL DEFAULT FALSE,
    late_penalty_pct       DECIMAL(5,2),
    brief_s3_key           VARCHAR(500),
    brief_file_name        VARCHAR(255),
    status                 assessment_status NOT NULL DEFAULT 'DRAFT',
    created_at             TIMESTAMPTZ       NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMPTZ       NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_assessment_uuid       UNIQUE (uuid),
    CONSTRAINT fk_assessment_module     FOREIGN KEY (module_id)
                                        REFERENCES course_module (id) ON DELETE CASCADE,
    CONSTRAINT fk_assessment_created_by FOREIGN KEY (created_by)
                                        REFERENCES app_user (id) ON DELETE RESTRICT,
    CONSTRAINT chk_assessment_marks     CHECK (total_marks > 0),
    CONSTRAINT chk_assessment_weight    CHECK (weight_pct IS NULL OR weight_pct BETWEEN 0 AND 100),
    CONSTRAINT chk_assessment_penalty   CHECK (late_penalty_pct IS NULL OR late_penalty_pct BETWEEN 0 AND 100),
    CONSTRAINT chk_assessment_visible   CHECK (visible_from IS NULL OR visible_from <= due_date),
    CONSTRAINT chk_assessment_brief    CHECK (
        (brief_s3_key IS NOT NULL AND brief_file_name IS NOT NULL)
        OR (brief_s3_key IS NULL AND brief_file_name IS NULL)
    )
);

COMMENT ON COLUMN assessment.brief_s3_key IS 'S3: assessments/{module_uuid}/{uuid}/brief.pdf — document displayed inline for students.';
COMMENT ON COLUMN assessment.brief_file_name IS 'Original file name of the assessment brief (e.g. case_study.pdf).';
