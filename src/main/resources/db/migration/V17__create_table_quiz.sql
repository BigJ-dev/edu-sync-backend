-- =============================================================================
-- EduSync - V17: quiz (belongs to MODULE)
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE quiz_status AS ENUM ('DRAFT', 'PUBLISHED', 'CLOSED');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS quiz (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                  UUID          NOT NULL DEFAULT gen_random_uuid(),
    module_id             BIGINT        NOT NULL,
    class_session_id      BIGINT,
    created_by            BIGINT        NOT NULL,
    title                 VARCHAR(255)  NOT NULL,
    description           TEXT,
    time_limit_minutes    INT,
    total_marks           DECIMAL(6,2)  NOT NULL,
    pass_mark_pct         DECIMAL(5,2)  NOT NULL DEFAULT 50,
    weight_pct            DECIMAL(5,2),
    max_attempts          INT           NOT NULL DEFAULT 1,
    shuffle_questions     BOOLEAN       NOT NULL DEFAULT FALSE,
    show_answers_after    BOOLEAN       NOT NULL DEFAULT TRUE,
    document_s3_key       VARCHAR(500),
    document_name         VARCHAR(255),
    visible_from          TIMESTAMPTZ,
    visible_until         TIMESTAMPTZ,
    status                quiz_status   NOT NULL DEFAULT 'DRAFT',
    created_at            TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ   NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_quiz_uuid        UNIQUE (uuid),
    CONSTRAINT fk_quiz_module      FOREIGN KEY (module_id)
                                   REFERENCES course_module (id) ON DELETE CASCADE,
    CONSTRAINT fk_quiz_session     FOREIGN KEY (class_session_id)
                                   REFERENCES class_session (id) ON DELETE SET NULL,
    CONSTRAINT fk_quiz_created_by  FOREIGN KEY (created_by)
                                   REFERENCES app_user (id) ON DELETE RESTRICT,
    CONSTRAINT chk_quiz_marks      CHECK (total_marks > 0),
    CONSTRAINT chk_quiz_pass       CHECK (pass_mark_pct BETWEEN 0 AND 100),
    CONSTRAINT chk_quiz_weight     CHECK (weight_pct IS NULL OR weight_pct BETWEEN 0 AND 100),
    CONSTRAINT chk_quiz_attempts   CHECK (max_attempts >= 1),
    CONSTRAINT chk_quiz_time_limit CHECK (time_limit_minutes IS NULL OR time_limit_minutes > 0),
    CONSTRAINT chk_quiz_visibility CHECK (visible_until IS NULL OR visible_from IS NULL
                                         OR visible_until > visible_from),
    CONSTRAINT chk_quiz_document   CHECK (
        (document_s3_key IS NOT NULL AND document_name IS NOT NULL)
        OR (document_s3_key IS NULL AND document_name IS NULL)
    )
);

COMMENT ON COLUMN quiz.document_s3_key IS 'S3: quizzes/{module_uuid}/{uuid}/case_study.pdf — optional document displayed inline alongside quiz questions.';
COMMENT ON COLUMN quiz.document_name IS 'Original file name of the attached document (e.g. case_study.pdf).';
