-- =============================================================================
-- EduSync - V48: Grade weight categories for weighted final grade calculation
-- Each course defines categories (e.g., Assignments 40%, Quizzes 20%, Exam 40%)
-- Assessments and quizzes are mapped to a category for weighted aggregation
-- =============================================================================

SET search_path TO edusync;

CREATE TYPE grade_category_type AS ENUM ('ASSIGNMENT', 'QUIZ', 'PROJECT', 'PRACTICAL', 'PRESENTATION', 'EXAM', 'OTHER');

CREATE TABLE IF NOT EXISTS grade_weight_category (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid            UUID                  NOT NULL DEFAULT gen_random_uuid(),
    course_id       BIGINT                NOT NULL,
    name            VARCHAR(100)          NOT NULL,
    category_type   grade_category_type   NOT NULL,
    weight_pct      NUMERIC(5,2)          NOT NULL,
    sort_order      INT                   NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ           NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ           NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_grade_weight_uuid          UNIQUE (uuid),
    CONSTRAINT fk_grade_weight_course        FOREIGN KEY (course_id)
                                             REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT uk_grade_weight_course_type   UNIQUE (course_id, category_type),
    CONSTRAINT chk_weight_pct_range          CHECK (weight_pct >= 0 AND weight_pct <= 100)
);

COMMENT ON TABLE grade_weight_category IS 'Defines grade weight categories per course for weighted final grade calculation.';
COMMENT ON COLUMN grade_weight_category.weight_pct IS 'Percentage weight of this category in the final grade (all categories should sum to 100).';
COMMENT ON COLUMN grade_weight_category.category_type IS 'Maps to assessment types and quizzes for automatic aggregation.';
