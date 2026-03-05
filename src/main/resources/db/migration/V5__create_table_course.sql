-- =============================================================================
-- EduSync - V5: course (root aggregate)
-- A course contains modules. Modules contain sessions, materials,
-- assessments, and quizzes.
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS course (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                UUID            NOT NULL DEFAULT gen_random_uuid(),
    code                VARCHAR(20)     NOT NULL,
    title               VARCHAR(255)    NOT NULL,
    description         TEXT,
    thumbnail_s3_key    VARCHAR(500),
    lecturer_id         BIGINT          NOT NULL,
    start_date          DATE            NOT NULL,
    end_date            DATE            NOT NULL,
    min_attendance_pct  INT             NOT NULL DEFAULT 80,
    max_students        INT,
    status              course_status   NOT NULL DEFAULT 'DRAFT',
    created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_course_uuid          UNIQUE (uuid),
    CONSTRAINT uk_course_code          UNIQUE (code),
    CONSTRAINT fk_course_lecturer      FOREIGN KEY (lecturer_id)
                                       REFERENCES app_user (id) ON DELETE RESTRICT,
    CONSTRAINT chk_course_dates        CHECK (end_date >= start_date),
    CONSTRAINT chk_course_attendance   CHECK (min_attendance_pct BETWEEN 0 AND 100),
    CONSTRAINT chk_course_max_students CHECK (max_students IS NULL OR max_students > 0)
);

COMMENT ON COLUMN course.thumbnail_s3_key IS 'S3 key: courses/{uuid}/thumbnail.jpg';
