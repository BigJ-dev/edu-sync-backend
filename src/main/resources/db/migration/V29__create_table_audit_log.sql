-- =============================================================================
-- EduSync - V29: audit_log
-- Immutable. No FKs on entity references — survives entity deletion.
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE audit_action AS ENUM (
        'CREATE', 'UPDATE', 'DELETE', 'STATUS_CHANGE',
        'GRADE_ASSIGNED', 'GRADE_CHANGED',
        'ATTENDANCE_OVERRIDE', 'ENROLLMENT_CHANGE',
        'ESCALATION', 'FOCUS_MODE_TOGGLE',
        'CERTIFICATE_ISSUED', 'PERMISSION_CHANGE'
    );
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS audit_log (
    id                      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    action                  audit_action   NOT NULL,
    entity_type             VARCHAR(50)    NOT NULL,
    entity_id               BIGINT         NOT NULL,
    entity_uuid             UUID,
    performed_by_type       sender_type,
    performed_by_student_id BIGINT,
    performed_by_user_id    BIGINT,
    field_name              VARCHAR(100),
    old_value               TEXT,
    new_value               TEXT,
    description             TEXT,
    course_id               BIGINT,
    ip_address              VARCHAR(45),
    user_agent              VARCHAR(500),
    created_at              TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_audit_performer CHECK (
        (performed_by_type = 'STUDENT' AND performed_by_student_id IS NOT NULL)
        OR (performed_by_type IN ('LECTURER', 'ADMIN') AND performed_by_user_id IS NOT NULL)
        OR (performed_by_type IS NULL)
    )
);
