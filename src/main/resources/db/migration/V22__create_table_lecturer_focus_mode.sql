-- =============================================================================
-- EduSync - V22: lecturer_focus_mode (WITH MODULE SUPPORT)
-- Supports both course-wide and module-specific focus mode
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS lecturer_focus_mode (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    lecturer_id     BIGINT        NOT NULL,
    course_id       BIGINT        NOT NULL,
    module_id       BIGINT,
    is_active       BOOLEAN       NOT NULL DEFAULT FALSE,
    reason          VARCHAR(255),
    activated_at    TIMESTAMPTZ,
    scheduled_end   TIMESTAMPTZ,
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_focus_lecturer                FOREIGN KEY (lecturer_id)
                                                REFERENCES app_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_focus_course                  FOREIGN KEY (course_id)
                                                REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT fk_focus_module                  FOREIGN KEY (module_id)
                                                REFERENCES course_module (id) ON DELETE CASCADE,
    CONSTRAINT chk_focus_schedule               CHECK (scheduled_end IS NULL OR activated_at IS NOT NULL),
    CONSTRAINT chk_focus_end_after_start        CHECK (scheduled_end IS NULL OR scheduled_end > activated_at)
);

CREATE UNIQUE INDEX uk_focus_lecturer_course_module ON lecturer_focus_mode (lecturer_id, course_id, COALESCE(module_id, -1));

COMMENT ON TABLE lecturer_focus_mode IS 'Focus mode blocks students from creating new message threads. Supports course-wide (module_id IS NULL) or module-specific (module_id IS NOT NULL).';
COMMENT ON COLUMN lecturer_focus_mode.module_id IS 'NULL = course-wide focus mode. NOT NULL = module-specific focus mode (e.g., during Module 3 exam).';
