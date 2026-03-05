-- =============================================================================
-- EduSync - V21: quiz_answer (WITH requires_manual_grading flag)
-- For MULTIPLE_CHOICE/TRUE_FALSE: selected_option_id is set.
-- For MULTI_SELECT: options stored in quiz_answer_option join table.
-- For SHORT_ANSWER: answer_text is set.
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS quiz_answer (
    id                       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    quiz_attempt_id          BIGINT        NOT NULL,
    question_id              BIGINT        NOT NULL,
    selected_option_id       BIGINT,
    answer_text              VARCHAR(2000),
    is_correct               BOOLEAN,
    marks_awarded            DECIMAL(6,2)  NOT NULL DEFAULT 0,
    requires_manual_grading  BOOLEAN       NOT NULL DEFAULT FALSE,
    answered_at              TIMESTAMPTZ   NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_answer_attempt_question UNIQUE (quiz_attempt_id, question_id),
    CONSTRAINT fk_answer_attempt          FOREIGN KEY (quiz_attempt_id)
                                          REFERENCES quiz_attempt (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_question         FOREIGN KEY (question_id)
                                          REFERENCES quiz_question (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_option           FOREIGN KEY (selected_option_id)
                                          REFERENCES quiz_question_option (id) ON DELETE SET NULL,
    CONSTRAINT chk_answer_marks           CHECK (marks_awarded >= 0)
);

COMMENT ON COLUMN quiz_answer.requires_manual_grading IS 'TRUE for SHORT_ANSWER questions that need lecturer review. Auto-set by trigger based on question_type.';
COMMENT ON COLUMN quiz_answer.selected_option_id IS 'Set for MULTIPLE_CHOICE and TRUE_FALSE. NULL for MULTI_SELECT (uses quiz_answer_option) and SHORT_ANSWER (uses answer_text).';

-- =============================================================================
-- quiz_answer_option: Join table for MULTI_SELECT answers.
-- One row per selected option per answer.
-- =============================================================================

CREATE TABLE IF NOT EXISTS quiz_answer_option (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    answer_id       BIGINT  NOT NULL,
    option_id       BIGINT  NOT NULL,

    CONSTRAINT uk_answer_option       UNIQUE (answer_id, option_id),
    CONSTRAINT fk_answer_opt_answer   FOREIGN KEY (answer_id)
                                      REFERENCES quiz_answer (id) ON DELETE CASCADE,
    CONSTRAINT fk_answer_opt_option   FOREIGN KEY (option_id)
                                      REFERENCES quiz_question_option (id) ON DELETE CASCADE
);

COMMENT ON TABLE quiz_answer_option IS 'Join table for MULTI_SELECT quiz answers. Each row is one selected option.';
