-- =============================================================================
-- EduSync - V20: quiz_attempt
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE quiz_attempt_status AS ENUM ('IN_PROGRESS', 'COMPLETED', 'TIMED_OUT');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS quiz_attempt (
    id               BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid             UUID                 NOT NULL DEFAULT gen_random_uuid(),
    quiz_id          BIGINT               NOT NULL,
    student_id       BIGINT               NOT NULL,
    attempt_number   INT                  NOT NULL,
    started_at       TIMESTAMPTZ          NOT NULL DEFAULT NOW(),
    completed_at     TIMESTAMPTZ,
    score            DECIMAL(6,2),
    score_pct        DECIMAL(5,2),
    passed           BOOLEAN,
    status           quiz_attempt_status  NOT NULL DEFAULT 'IN_PROGRESS',
    created_at       TIMESTAMPTZ          NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_attempt_uuid             UNIQUE (uuid),
    CONSTRAINT uk_attempt_quiz_student_num UNIQUE (quiz_id, student_id, attempt_number),
    CONSTRAINT fk_attempt_quiz             FOREIGN KEY (quiz_id)
                                           REFERENCES quiz (id) ON DELETE CASCADE,
    CONSTRAINT fk_attempt_student          FOREIGN KEY (student_id)
                                           REFERENCES student (id) ON DELETE RESTRICT,
    CONSTRAINT chk_attempt_number          CHECK (attempt_number >= 1),
    CONSTRAINT chk_attempt_score           CHECK (score IS NULL OR score >= 0),
    CONSTRAINT chk_attempt_score_pct       CHECK (score_pct IS NULL OR score_pct BETWEEN 0 AND 100),
    CONSTRAINT chk_attempt_completed       CHECK (
        (status = 'IN_PROGRESS' AND completed_at IS NULL) OR (status != 'IN_PROGRESS')
    )
);
