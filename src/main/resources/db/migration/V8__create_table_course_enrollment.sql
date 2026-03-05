-- =============================================================================
-- EduSync - V8: course_enrollment (M:M student <-> course with lifecycle)
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS course_enrollment (
    id                   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_id            BIGINT              NOT NULL,
    student_id           BIGINT              NOT NULL,
    enrolled_at          TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    status               enrollment_status   NOT NULL DEFAULT 'ENROLLED',
    final_attendance_pct DECIMAL(5,2),
    final_grade          DECIMAL(5,2),
    withdrawn_at         TIMESTAMPTZ,
    withdrawal_reason    VARCHAR(500),
    is_blocked           BOOLEAN        NOT NULL DEFAULT FALSE,
    blocked_at           TIMESTAMPTZ,
    blocked_by           BIGINT,
    blocked_reason       VARCHAR(500),

    CONSTRAINT uk_enrollment            UNIQUE (course_id, student_id),
    CONSTRAINT fk_enrollment_course     FOREIGN KEY (course_id)
                                        REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_student    FOREIGN KEY (student_id)
                                        REFERENCES student (id) ON DELETE RESTRICT,
    CONSTRAINT fk_enrollment_blocked_by FOREIGN KEY (blocked_by)
                                        REFERENCES app_user (id) ON DELETE SET NULL,
    CONSTRAINT chk_enrollment_att_pct   CHECK (final_attendance_pct IS NULL
                                              OR final_attendance_pct BETWEEN 0 AND 100),
    CONSTRAINT chk_enrollment_grade     CHECK (final_grade IS NULL
                                              OR final_grade BETWEEN 0 AND 100),
    CONSTRAINT chk_enrollment_withdrawn CHECK (
        (status = 'WITHDRAWN' AND withdrawn_at IS NOT NULL)
        OR (status != 'WITHDRAWN')
    ),
    CONSTRAINT chk_enrollment_blocked   CHECK (
        (is_blocked = TRUE AND blocked_at IS NOT NULL AND blocked_by IS NOT NULL AND blocked_reason IS NOT NULL)
        OR (is_blocked = FALSE AND blocked_at IS NULL AND blocked_by IS NULL AND blocked_reason IS NULL)
    )
);

COMMENT ON COLUMN course_enrollment.is_blocked IS 'When TRUE, student cannot access this course or its modules. Independent from enrollment status.';
COMMENT ON COLUMN course_enrollment.blocked_by IS 'The lecturer or admin who blocked the student from this course.';
