-- =============================================================================
-- EduSync - V27: broadcast_recipient
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS broadcast_recipient (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    broadcast_message_id  BIGINT       NOT NULL,
    student_id            BIGINT       NOT NULL,
    read_at               TIMESTAMPTZ,
    email_sent            BOOLEAN      NOT NULL DEFAULT FALSE,
    email_sent_at         TIMESTAMPTZ,

    CONSTRAINT uk_bcast_recipient     UNIQUE (broadcast_message_id, student_id),
    CONSTRAINT fk_bcast_recip_message FOREIGN KEY (broadcast_message_id)
                                      REFERENCES broadcast_message (id) ON DELETE CASCADE,
    CONSTRAINT fk_bcast_recip_student FOREIGN KEY (student_id)
                                      REFERENCES student (id) ON DELETE CASCADE,
    CONSTRAINT chk_bcast_email_sent   CHECK (
        (email_sent = TRUE AND email_sent_at IS NOT NULL) OR (email_sent = FALSE)
    )
);
