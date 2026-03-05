-- =============================================================================
-- EduSync - V28: notification (SIMPLIFIED + MODULE CONTEXT)
-- Polymorphic pattern simplified: single recipient_id + recipient_type
-- Added module_id for module-specific notification context
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE notification_type AS ENUM (
        'SESSION_SCHEDULED', 'SESSION_STARTING', 'SESSION_RECORDING_READY',
        'ASSESSMENT_PUBLISHED', 'ASSESSMENT_GRADED', 'ASSESSMENT_DUE_REMINDER',
        'QUIZ_PUBLISHED', 'QUIZ_GRADED',
        'MATERIAL_UPLOADED', 'MODULE_PUBLISHED',
        'MESSAGE_RECEIVED', 'THREAD_ESCALATED',
        'BROADCAST_RECEIVED',
        'ENROLLMENT_CONFIRMED', 'CERTIFICATE_ISSUED', 'COURSE_COMPLETED',
        'ATTENDANCE_FLAGGED'
    );
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS notification (
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid               UUID              NOT NULL DEFAULT gen_random_uuid(),
    recipient_type     sender_type       NOT NULL,
    recipient_id       BIGINT            NOT NULL,
    notification_type  notification_type NOT NULL,
    title              VARCHAR(255)      NOT NULL,
    body               TEXT,
    course_id          BIGINT,
    module_id          BIGINT,
    entity_type        VARCHAR(50),
    entity_id          BIGINT,
    read_at            TIMESTAMPTZ,
    dismissed_at       TIMESTAMPTZ,
    created_at         TIMESTAMPTZ       NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_notification_uuid UNIQUE (uuid),
    CONSTRAINT fk_notif_course      FOREIGN KEY (course_id)
                                    REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT fk_notif_module      FOREIGN KEY (module_id)
                                    REFERENCES course_module (id) ON DELETE CASCADE
);

COMMENT ON TABLE notification IS 'Platform notifications for students, lecturers, and admins. Recipient identity resolved at application layer.';
COMMENT ON COLUMN notification.recipient_id IS 'References student.id or app_user.id based on recipient_type. No FK for flexibility.';
COMMENT ON COLUMN notification.module_id IS 'Module context for notifications like "New material in Module 2". Nullable.';
