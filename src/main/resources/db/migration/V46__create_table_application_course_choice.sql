CREATE TABLE edusync.application_course_choice
(
    id             BIGSERIAL PRIMARY KEY,
    application_id BIGINT      NOT NULL REFERENCES edusync.student_application (id) ON DELETE CASCADE,
    course_id      BIGINT      NOT NULL REFERENCES edusync.course (id),
    priority       INT         NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (application_id, course_id),
    UNIQUE (application_id, priority)
);

CREATE INDEX idx_app_course_choice_app_id ON edusync.application_course_choice (application_id);
