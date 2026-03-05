-- =============================================================================
-- EduSync - V10: attendance_record (WITH DENORMALIZED course_id & module_id)
-- Summary per student per session. Synced from Teams (online) or manually
-- recorded (physical). Denormalized course_id and module_id for fast queries.
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE attendance_status AS ENUM ('PRESENT', 'PARTIAL', 'ABSENT');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS attendance_record (
    id                     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    class_session_id       BIGINT             NOT NULL,
    student_id             BIGINT             NOT NULL,
    course_id              BIGINT             NOT NULL,
    module_id              BIGINT             NOT NULL,
    join_time              TIMESTAMPTZ,
    leave_time             TIMESTAMPTZ,
    total_duration_minutes INT,
    attendance_status      attendance_status  NOT NULL DEFAULT 'ABSENT',
    report_id              BIGINT,
    synced_from_teams      BOOLEAN            NOT NULL DEFAULT FALSE,

    manually_overridden    BOOLEAN            NOT NULL DEFAULT FALSE,
    override_by            BIGINT,
    override_reason        VARCHAR(500),
    created_at             TIMESTAMPTZ        NOT NULL DEFAULT NOW(),
    updated_at             TIMESTAMPTZ        NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_attendance_session_student UNIQUE (class_session_id, student_id),
    CONSTRAINT fk_attendance_session         FOREIGN KEY (class_session_id)
                                             REFERENCES class_session (id) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_student         FOREIGN KEY (student_id)
                                             REFERENCES student (id) ON DELETE RESTRICT,
    CONSTRAINT fk_attendance_course          FOREIGN KEY (course_id)
                                             REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_module          FOREIGN KEY (module_id)
                                             REFERENCES course_module (id) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_override_by     FOREIGN KEY (override_by)
                                             REFERENCES app_user (id) ON DELETE SET NULL,
    CONSTRAINT chk_attendance_duration       CHECK (total_duration_minutes IS NULL
                                                    OR total_duration_minutes >= 0),
    CONSTRAINT chk_attendance_times          CHECK (leave_time IS NULL OR join_time IS NOT NULL),
    CONSTRAINT chk_attendance_override       CHECK (
        (manually_overridden = TRUE AND override_by IS NOT NULL AND override_reason IS NOT NULL)
        OR (manually_overridden = FALSE)
    )
);

COMMENT ON TABLE attendance_record IS 'Per-student per-session attendance summary. course_id and module_id denormalized for fast queries.';
COMMENT ON COLUMN attendance_record.course_id IS 'Denormalized from class_session → course_module → course. Must match for consistency.';
COMMENT ON COLUMN attendance_record.module_id IS 'Denormalized from class_session → course_module. Must match for consistency.';
COMMENT ON COLUMN attendance_record.report_id IS 'FK to teams_attendance_report. Set when synced from Teams. NULL for physical sessions or manual entries. FK added in V41.';
