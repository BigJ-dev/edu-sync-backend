-- =============================================================================
-- EduSync - V6: course_module
-- Modules are the organizing layer within a course.
-- e.g. Course "Java Fundamentals" has:
--   Module 1: "Variables & Types"
--   Module 2: "OOP Concepts"
--   Module 3: "Collections Framework"
--
-- Sessions, materials, assessments, and quizzes belong to a module.
-- Modules are ordered via sort_order within a course.
-- A module can be LOCKED (not yet available to students) or PUBLISHED.
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS course_module (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid        UUID           NOT NULL DEFAULT gen_random_uuid(),
    course_id   BIGINT         NOT NULL,
    title       VARCHAR(255)   NOT NULL,
    description TEXT,
    sort_order  INT            NOT NULL,
    status      module_status  NOT NULL DEFAULT 'DRAFT',
    start_date  DATE,
    end_date    DATE,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_module_uuid          UNIQUE (uuid),
    CONSTRAINT uk_module_course_order  UNIQUE (course_id, sort_order),
    CONSTRAINT fk_module_course        FOREIGN KEY (course_id)
                                       REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT chk_module_sort         CHECK (sort_order >= 0),
    CONSTRAINT chk_module_dates        CHECK (
        end_date IS NULL OR start_date IS NULL OR end_date >= start_date
    )
);

COMMENT ON TABLE course_module IS 'Organizing layer within a course. Sessions, materials, assessments, quizzes belong to a module.';
