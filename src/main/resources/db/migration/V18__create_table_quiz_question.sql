-- =============================================================================
-- EduSync - V18: quiz_question (WITH UUID for builder UI)
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE question_type AS ENUM ('MULTIPLE_CHOICE', 'MULTI_SELECT', 'TRUE_FALSE', 'SHORT_ANSWER');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS quiz_question (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid            UUID           NOT NULL DEFAULT gen_random_uuid(),
    quiz_id         BIGINT         NOT NULL,
    question_text   TEXT           NOT NULL,
    question_type   question_type  NOT NULL,
    image_s3_key    VARCHAR(500),
    marks           DECIMAL(6,2)   NOT NULL,
    sort_order      INT            NOT NULL,
    explanation     TEXT,
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_question_uuid       UNIQUE (uuid),
    CONSTRAINT uk_question_quiz_order UNIQUE (quiz_id, sort_order),
    CONSTRAINT fk_question_quiz       FOREIGN KEY (quiz_id)
                                      REFERENCES quiz (id) ON DELETE CASCADE,
    CONSTRAINT chk_question_marks     CHECK (marks > 0),
    CONSTRAINT chk_question_sort      CHECK (sort_order >= 0)
);

COMMENT ON COLUMN quiz_question.uuid IS 'Stable identifier for quiz builder UI. Allows client-side tracking before DB persistence.';
COMMENT ON COLUMN quiz_question.image_s3_key IS 'S3: quizzes/{quiz_uuid}/questions/{id}/image.png';
