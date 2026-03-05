-- =============================================================================
-- EduSync - V16: rubric_grade (WITH graded_by and graded_at for audit trail)
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS rubric_grade (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    submission_id   BIGINT        NOT NULL,
    criteria_id     BIGINT        NOT NULL,
    marks_awarded   DECIMAL(6,2)  NOT NULL DEFAULT 0,
    comment         TEXT,
    graded_by       BIGINT,
    graded_at       TIMESTAMPTZ,

    CONSTRAINT uk_rubric_grade      UNIQUE (submission_id, criteria_id),
    CONSTRAINT fk_rgrade_submission FOREIGN KEY (submission_id)
                                    REFERENCES assessment_submission (id) ON DELETE CASCADE,
    CONSTRAINT fk_rgrade_criteria   FOREIGN KEY (criteria_id)
                                    REFERENCES rubric_criteria (id) ON DELETE CASCADE,
    CONSTRAINT fk_rgrade_graded_by  FOREIGN KEY (graded_by)
                                    REFERENCES app_user (id) ON DELETE SET NULL,
    CONSTRAINT chk_rgrade_marks     CHECK (marks_awarded >= 0)
);

COMMENT ON COLUMN rubric_grade.graded_by IS 'Lecturer/admin who graded this criterion. Tracks grading responsibility.';
COMMENT ON COLUMN rubric_grade.graded_at IS 'Timestamp when this criterion was graded. Auto-set by trigger.';
