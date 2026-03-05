-- =============================================================================
-- EduSync - V19: quiz_question_option (WITH UUID for builder UI)
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS quiz_question_option (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid          UUID            NOT NULL DEFAULT gen_random_uuid(),
    question_id   BIGINT          NOT NULL,
    option_text   VARCHAR(1000)   NOT NULL,
    is_correct    BOOLEAN         NOT NULL DEFAULT FALSE,
    feedback      TEXT,
    sort_order    INT             NOT NULL,
    created_at    TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_option_uuid           UNIQUE (uuid),
    CONSTRAINT uk_option_question_order UNIQUE (question_id, sort_order),
    CONSTRAINT fk_option_question       FOREIGN KEY (question_id)
                                        REFERENCES quiz_question (id) ON DELETE CASCADE,
    CONSTRAINT chk_option_sort          CHECK (sort_order >= 0)
);

COMMENT ON COLUMN quiz_question_option.uuid IS 'Stable identifier for quiz builder UI. Allows drag-drop reordering in client.';
COMMENT ON COLUMN quiz_question_option.feedback IS 'Per-option feedback shown after quiz completion. E.g. "Incorrect — Java uses 0-based indexing."';
