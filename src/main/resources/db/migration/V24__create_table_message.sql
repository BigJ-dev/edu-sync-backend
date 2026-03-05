-- =============================================================================
-- EduSync - V24: message (SIMPLIFIED)
-- Polymorphic pattern simplified: single sender_id + sender_type
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE sender_type AS ENUM ('STUDENT', 'LECTURER', 'ADMIN');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS message (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                UUID          NOT NULL DEFAULT gen_random_uuid(),
    thread_id           BIGINT        NOT NULL,
    sender_type         sender_type   NOT NULL,
    sender_id           BIGINT,
    body                TEXT          NOT NULL,
    attachment_s3_key   VARCHAR(500),
    attachment_name     VARCHAR(255),
    is_system_message   BOOLEAN       NOT NULL DEFAULT FALSE,
    edited_at           TIMESTAMPTZ,
    deleted_at          TIMESTAMPTZ,
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_message_uuid     UNIQUE (uuid),
    CONSTRAINT fk_message_thread   FOREIGN KEY (thread_id)
                                   REFERENCES message_thread (id) ON DELETE CASCADE,
    CONSTRAINT chk_message_sender  CHECK (
        (is_system_message = FALSE AND sender_id IS NOT NULL)
        OR (is_system_message = TRUE AND sender_id IS NULL)
    )
);

COMMENT ON TABLE  message              IS 'Thread messages. Sender identity resolved via sender_type at application layer.';
COMMENT ON COLUMN message.sender_id    IS 'References student.id if sender_type=STUDENT, app_user.id if LECTURER/ADMIN. No FK for flexibility.';
COMMENT ON COLUMN message.attachment_s3_key IS 'S3: messages/{thread_uuid}/{uuid}/{file}';
COMMENT ON COLUMN message.deleted_at   IS 'Soft delete with audit timestamp. NULL = not deleted.';
