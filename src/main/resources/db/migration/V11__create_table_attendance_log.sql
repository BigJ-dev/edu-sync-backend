-- =============================================================================
-- EduSync - V11: attendance_log
-- Raw join/leave events. For online sessions, matched to students via email.
-- For physical sessions, manually recorded by lecturer.
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE attendance_event AS ENUM ('JOIN', 'LEAVE');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS attendance_log (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    attendance_record_id  BIGINT            NOT NULL,
    event_type            attendance_event  NOT NULL,
    event_time            TIMESTAMPTZ       NOT NULL,
    teams_identity_email  VARCHAR(255),
    created_at            TIMESTAMPTZ       NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_att_log_record FOREIGN KEY (attendance_record_id)
                                 REFERENCES attendance_record (id) ON DELETE CASCADE
);

COMMENT ON COLUMN attendance_log.teams_identity_email IS 'Email from Graph attendanceRecord. Matched to student.email. NULL for physical sessions.';
