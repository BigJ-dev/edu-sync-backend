-- =============================================================================
-- EduSync - V47: Institution-wide announcements / news feed
-- Covers exam schedules, holidays, campus news, and general announcements
-- =============================================================================

SET search_path TO edusync;

CREATE TYPE announcement_category AS ENUM ('GENERAL', 'EXAM_SCHEDULE', 'HOLIDAY', 'CAMPUS_NEWS', 'MAINTENANCE', 'EVENT');
CREATE TYPE announcement_status   AS ENUM ('DRAFT', 'PUBLISHED', 'ARCHIVED');

CREATE TABLE IF NOT EXISTS announcement (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid            UUID                  NOT NULL DEFAULT gen_random_uuid(),
    title           VARCHAR(255)          NOT NULL,
    body            TEXT                  NOT NULL,
    category        announcement_category NOT NULL DEFAULT 'GENERAL',
    status          announcement_status   NOT NULL DEFAULT 'DRAFT',
    pinned          BOOLEAN               NOT NULL DEFAULT FALSE,
    published_by    BIGINT                NOT NULL,
    published_at    TIMESTAMPTZ,
    expires_at      TIMESTAMPTZ,
    attachment_s3_key VARCHAR(500),
    attachment_name VARCHAR(255),
    created_at      TIMESTAMPTZ           NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ           NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_announcement_uuid       UNIQUE (uuid),
    CONSTRAINT fk_announcement_publisher  FOREIGN KEY (published_by)
                                          REFERENCES app_user (id) ON DELETE RESTRICT
);

COMMENT ON TABLE announcement IS 'Institution-wide announcements visible to all users — exam schedules, holidays, campus news, etc.';
COMMENT ON COLUMN announcement.pinned IS 'Pinned announcements appear at the top of the news feed.';
COMMENT ON COLUMN announcement.expires_at IS 'Optional expiry — expired announcements are hidden from the feed but not deleted.';
