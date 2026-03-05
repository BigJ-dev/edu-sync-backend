-- =============================================================================
-- EduSync - V33: teams_webhook_event
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS teams_webhook_event (
    id             BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id       VARCHAR(512),
    resource_url   TEXT,
    change_type    VARCHAR(50),
    payload        JSONB,
    processed      BOOLEAN      NOT NULL DEFAULT FALSE,
    processed_at   TIMESTAMPTZ,
    error_message  TEXT,
    retry_count    INT          NOT NULL DEFAULT 0,
    created_at     TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_webhook_retry CHECK (retry_count >= 0)
);
