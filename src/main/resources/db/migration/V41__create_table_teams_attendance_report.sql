-- =============================================================================
-- EduSync - V41: teams_attendance_report
-- Stores the raw attendance report fetched from Microsoft Graph API.
-- One report per online class session. Links to attendance_record for
-- traceability of how each record was derived.
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE report_sync_status AS ENUM ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED');
EXCEPTION WHEN duplicate_object THEN
    -- Safe to drop and recreate: no table uses report_sync_status before V41.
    IF NOT EXISTS (
        SELECT 1 FROM pg_enum e JOIN pg_type t ON e.enumtypid = t.oid
        WHERE t.typname = 'report_sync_status' AND e.enumlabel = 'PROCESSING'
    ) THEN
        DROP TYPE report_sync_status;
        CREATE TYPE report_sync_status AS ENUM ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED');
    END IF;
END $$;

CREATE TABLE IF NOT EXISTS teams_attendance_report (
    id                        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                      UUID               NOT NULL DEFAULT gen_random_uuid(),
    class_session_id          BIGINT             NOT NULL,
    graph_report_id           VARCHAR(512)       NOT NULL,
    meeting_start             TIMESTAMPTZ,
    meeting_end               TIMESTAMPTZ,
    total_participant_count   INT,
    raw_json                  JSONB              NOT NULL,
    sync_status               report_sync_status NOT NULL DEFAULT 'PENDING',
    synced_at                 TIMESTAMPTZ,
    error_message             TEXT,
    retry_count               INT                NOT NULL DEFAULT 0,
    created_at                TIMESTAMPTZ        NOT NULL DEFAULT NOW(),
    updated_at                TIMESTAMPTZ        NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_teams_report_uuid           UNIQUE (uuid),
    CONSTRAINT uk_teams_report_graph_id       UNIQUE (graph_report_id),
    CONSTRAINT fk_teams_report_session        FOREIGN KEY (class_session_id)
                                              REFERENCES class_session (id) ON DELETE CASCADE,
    CONSTRAINT chk_teams_report_retry         CHECK (retry_count >= 0),
    CONSTRAINT chk_teams_report_participant   CHECK (total_participant_count IS NULL
                                                     OR total_participant_count >= 0),
    CONSTRAINT chk_teams_report_sync          CHECK (
        (sync_status = 'COMPLETED' AND synced_at IS NOT NULL)
        OR (sync_status != 'COMPLETED')
    )
);

COMMENT ON TABLE teams_attendance_report IS 'Raw attendance report from Microsoft Graph API. One per online class session. Audit trail for attendance sync.';
COMMENT ON COLUMN teams_attendance_report.graph_report_id IS 'The meetingAttendanceReport ID from Graph API.';
COMMENT ON COLUMN teams_attendance_report.raw_json IS 'Full JSON response from Graph API including attendanceRecords array. Stored for audit and reprocessing.';
COMMENT ON COLUMN teams_attendance_report.sync_status IS 'PENDING = fetched but not processed. PROCESSING = matching in progress. COMPLETED = attendance_records created. FAILED = error during processing.';

-- =============================================================================
-- Deferred FK: attendance_record.report_id → teams_attendance_report
-- Column was added in V10 but FK deferred here because this table didn't exist yet.
-- =============================================================================

ALTER TABLE attendance_record
    ADD CONSTRAINT fk_attendance_report FOREIGN KEY (report_id)
    REFERENCES teams_attendance_report (id) ON DELETE SET NULL;

-- Indexes moved here from V36 because this table is created in V41, after V36 runs
CREATE INDEX IF NOT EXISTS idx_teams_report_session ON teams_attendance_report (class_session_id);
CREATE INDEX IF NOT EXISTS idx_teams_report_status  ON teams_attendance_report (sync_status) WHERE sync_status != 'COMPLETED';

-- Trigger moved here from V37 because this table is created in V41, after V37 runs
CREATE TRIGGER trg_teams_attendance_report_updated_at
    BEFORE UPDATE ON teams_attendance_report
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
