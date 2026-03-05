-- =============================================================================
-- EduSync - V9: class_session
-- Supports ONLINE (Teams) and PHYSICAL (in-person) classes.
-- Belongs to a MODULE within a course.
-- Online attendance synced from Teams via email matching.
-- Physical attendance recorded manually by lecturer.
-- =============================================================================

SET search_path TO edusync;

-- Guard: create types if they were not present when V1 ran
DO $$ BEGIN
    CREATE TYPE session_type AS ENUM ('ONLINE', 'PHYSICAL');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

DO $$ BEGIN
    CREATE TYPE session_status AS ENUM ('SCHEDULED', 'LIVE', 'COMPLETED', 'CANCELLED');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS class_session (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                  UUID            NOT NULL DEFAULT gen_random_uuid(),
    module_id             BIGINT          NOT NULL,
    lecturer_id           BIGINT          NOT NULL,
    title                 VARCHAR(255)    NOT NULL,
    description           TEXT,
    session_type          session_type    NOT NULL,
    session_number        INT             NOT NULL,
    scheduled_start       TIMESTAMPTZ     NOT NULL,
    scheduled_end         TIMESTAMPTZ     NOT NULL,
    actual_start          TIMESTAMPTZ,
    actual_end            TIMESTAMPTZ,

    -- Online (Teams) fields
    teams_meeting_id      VARCHAR(512),
    teams_join_url        TEXT,
    recording_s3_key      VARCHAR(500),

    -- Physical fields
    venue                 VARCHAR(500),

    status                session_status  NOT NULL DEFAULT 'SCHEDULED',
    created_at            TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_class_session_uuid             UNIQUE (uuid),
    CONSTRAINT uk_class_session_module_number    UNIQUE (module_id, session_number),
    CONSTRAINT fk_class_session_module           FOREIGN KEY (module_id)
                                                 REFERENCES course_module (id) ON DELETE CASCADE,
    CONSTRAINT fk_class_session_lecturer         FOREIGN KEY (lecturer_id)
                                                 REFERENCES app_user (id) ON DELETE RESTRICT,
    CONSTRAINT chk_class_session_schedule        CHECK (scheduled_end > scheduled_start),
    CONSTRAINT chk_class_session_actual          CHECK (actual_end IS NULL OR actual_start IS NOT NULL),
    CONSTRAINT chk_class_session_number          CHECK (session_number > 0),

    -- ONLINE requires Teams fields; PHYSICAL requires venue
    CONSTRAINT chk_class_session_online          CHECK (
        session_type != 'ONLINE'
        OR (teams_meeting_id IS NOT NULL AND teams_join_url IS NOT NULL)
    ),
    CONSTRAINT chk_class_session_physical        CHECK (
        session_type != 'PHYSICAL'
        OR venue IS NOT NULL
    )
);

COMMENT ON COLUMN class_session.session_type IS 'ONLINE = Teams meeting, PHYSICAL = in-person class';
COMMENT ON COLUMN class_session.venue IS 'Location/room for PHYSICAL sessions. NULL for ONLINE.';
COMMENT ON COLUMN class_session.recording_s3_key IS 'S3: sessions/{uuid}/recording.mp4';
COMMENT ON COLUMN class_session.teams_meeting_id IS 'Graph API onlineMeeting ID. Attendance synced via email matching. NULL for PHYSICAL.';
