-- =============================================================================
-- EduSync - V14: rubric_criteria (WITH UUID for builder UI)
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS rubric_criteria (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid            UUID           NOT NULL DEFAULT gen_random_uuid(),
    assessment_id   BIGINT         NOT NULL,
    title           VARCHAR(255)   NOT NULL,
    description     TEXT,
    max_marks       DECIMAL(6,2)   NOT NULL,
    sort_order      INT            NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_rubric_uuid             UNIQUE (uuid),
    CONSTRAINT uk_rubric_assessment_order UNIQUE (assessment_id, sort_order),
    CONSTRAINT fk_rubric_assessment       FOREIGN KEY (assessment_id)
                                          REFERENCES assessment (id) ON DELETE CASCADE,
    CONSTRAINT chk_rubric_marks           CHECK (max_marks > 0),
    CONSTRAINT chk_rubric_sort            CHECK (sort_order >= 0)
);

COMMENT ON COLUMN rubric_criteria.uuid IS 'Stable identifier for assessment rubric builder UI. Supports dynamic criteria creation.';
