-- =============================================================================
-- EduSync - V43: Add optional group_id to class_session
-- Allows a class session to target a specific student group within a course.
-- When null, the session targets all enrolled students.
-- =============================================================================

SET search_path TO edusync;

ALTER TABLE class_session
    ADD COLUMN group_id BIGINT;

ALTER TABLE class_session
    ADD CONSTRAINT fk_session_group FOREIGN KEY (group_id)
    REFERENCES course_group (id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_session_group ON class_session (group_id) WHERE group_id IS NOT NULL;
