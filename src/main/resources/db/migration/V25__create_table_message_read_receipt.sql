-- =============================================================================
-- EduSync - V25: message_read_receipt (SIMPLIFIED)
-- Composite PK pattern: (thread_id, reader_type, reader_id)
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS message_read_receipt (
    thread_id       BIGINT       NOT NULL,
    reader_type     sender_type  NOT NULL,
    reader_id       BIGINT       NOT NULL,
    last_read_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_read_receipt   PRIMARY KEY (thread_id, reader_type, reader_id),
    CONSTRAINT fk_read_thread    FOREIGN KEY (thread_id)
                                 REFERENCES message_thread (id) ON DELETE CASCADE
);

COMMENT ON TABLE message_read_receipt IS 'Per-participant read tracking. One row per (thread, participant). Reader identity resolved at application layer.';
COMMENT ON COLUMN message_read_receipt.reader_id IS 'References student.id if reader_type=STUDENT, app_user.id if LECTURER/ADMIN. No FK for flexibility.';
