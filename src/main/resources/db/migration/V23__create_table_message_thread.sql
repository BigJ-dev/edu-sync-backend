-- =============================================================================
-- EduSync - V23: message_thread
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE thread_status AS ENUM ('OPEN', 'RESOLVED', 'CLOSED');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE thread_priority AS ENUM ('NORMAL', 'URGENT');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS message_thread (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                  UUID             NOT NULL DEFAULT gen_random_uuid(),
    course_id             BIGINT           NOT NULL,
    student_id            BIGINT           NOT NULL,
    subject               VARCHAR(255)     NOT NULL,
    status                thread_status    NOT NULL DEFAULT 'OPEN',
    priority              thread_priority  NOT NULL DEFAULT 'NORMAL',
    is_escalated          BOOLEAN          NOT NULL DEFAULT FALSE,
    escalated_at          TIMESTAMPTZ,
    escalated_by          BIGINT,
    last_message_at       TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    student_unread_count  INT              NOT NULL DEFAULT 0,
    staff_unread_count    INT              NOT NULL DEFAULT 1,
    created_at            TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ      NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_thread_uuid            UNIQUE (uuid),
    CONSTRAINT fk_thread_course          FOREIGN KEY (course_id)
                                         REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT fk_thread_student         FOREIGN KEY (student_id)
                                         REFERENCES student (id) ON DELETE RESTRICT,
    CONSTRAINT fk_thread_escalated_by    FOREIGN KEY (escalated_by)
                                         REFERENCES app_user (id) ON DELETE SET NULL,
    CONSTRAINT chk_thread_unread_student CHECK (student_unread_count >= 0),
    CONSTRAINT chk_thread_unread_staff   CHECK (staff_unread_count >= 0),
    CONSTRAINT chk_thread_escalation     CHECK (
        (is_escalated = TRUE AND escalated_at IS NOT NULL) OR (is_escalated = FALSE)
    )
);
