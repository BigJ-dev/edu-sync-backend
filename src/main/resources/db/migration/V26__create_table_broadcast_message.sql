-- =============================================================================
-- EduSync - V26: broadcast_message (WITH MODULE SUPPORT)
-- Supports course-wide, module-specific, session-specific, and global broadcasts
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE broadcast_target AS ENUM ('ALL_COURSE', 'SPECIFIC_MODULE', 'SPECIFIC_SESSION', 'SPECIFIC_STUDENTS', 'GLOBAL');
EXCEPTION WHEN duplicate_object THEN
    -- Type exists but may be missing values added to V1 after it ran.
    -- Safe to drop and recreate here because no table uses broadcast_target before V26.
    IF NOT EXISTS (
        SELECT 1 FROM pg_enum e JOIN pg_type t ON e.enumtypid = t.oid
        WHERE t.typname = 'broadcast_target' AND e.enumlabel = 'SPECIFIC_MODULE'
    ) THEN
        DROP TYPE broadcast_target;
        CREATE TYPE broadcast_target AS ENUM ('ALL_COURSE', 'SPECIFIC_MODULE', 'SPECIFIC_SESSION', 'SPECIFIC_STUDENTS', 'GLOBAL');
    END IF;
END $$;

DO $$ BEGIN
    CREATE TYPE broadcast_priority AS ENUM ('LOW', 'NORMAL', 'URGENT');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS broadcast_message (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                UUID               NOT NULL DEFAULT gen_random_uuid(),
    course_id           BIGINT,
    module_id           BIGINT,
    sent_by             BIGINT             NOT NULL,
    title               VARCHAR(255)       NOT NULL,
    body                TEXT               NOT NULL,
    target_type         broadcast_target   NOT NULL,
    target_session_id   BIGINT,
    attachment_s3_key   VARCHAR(500),
    attachment_name     VARCHAR(255),
    priority            broadcast_priority NOT NULL DEFAULT 'NORMAL',
    send_email          BOOLEAN            NOT NULL DEFAULT FALSE,
    sent_at             TIMESTAMPTZ        NOT NULL DEFAULT NOW(),
    created_at          TIMESTAMPTZ        NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_broadcast_uuid     UNIQUE (uuid),
    CONSTRAINT fk_broadcast_course   FOREIGN KEY (course_id)
                                     REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT fk_broadcast_module   FOREIGN KEY (module_id)
                                     REFERENCES course_module (id) ON DELETE CASCADE,
    CONSTRAINT fk_broadcast_sent_by  FOREIGN KEY (sent_by)
                                     REFERENCES app_user (id) ON DELETE RESTRICT,
    CONSTRAINT fk_broadcast_session  FOREIGN KEY (target_session_id)
                                     REFERENCES class_session (id) ON DELETE SET NULL,
    CONSTRAINT chk_broadcast_target  CHECK (
        (target_type = 'GLOBAL' AND course_id IS NULL AND module_id IS NULL)
        OR (target_type = 'ALL_COURSE' AND course_id IS NOT NULL AND module_id IS NULL)
        OR (target_type = 'SPECIFIC_MODULE' AND module_id IS NOT NULL)
        OR (target_type = 'SPECIFIC_SESSION' AND target_session_id IS NOT NULL)
        OR (target_type = 'SPECIFIC_STUDENTS')
    )
);

COMMENT ON TABLE broadcast_message IS 'One-way announcements from lecturer/admin. Supports course, module, session, student-specific, and global broadcasts.';
COMMENT ON COLUMN broadcast_message.module_id IS 'For SPECIFIC_MODULE target type. Allows module-specific announcements.';
