-- =============================================================================
-- EduSync - V31: course_group (WITH MODULE SUPPORT)
-- Supports both course-wide groups and module-specific project groups
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS course_group (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid        UUID           NOT NULL DEFAULT gen_random_uuid(),
    course_id   BIGINT         NOT NULL,
    module_id   BIGINT,
    name        VARCHAR(100)   NOT NULL,
    description TEXT,
    max_members INT,
    created_by  BIGINT         NOT NULL,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_group_uuid              UNIQUE (uuid),
    CONSTRAINT fk_group_course            FOREIGN KEY (course_id)
                                          REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT fk_group_module            FOREIGN KEY (module_id)
                                          REFERENCES course_module (id) ON DELETE CASCADE,
    CONSTRAINT fk_group_created_by        FOREIGN KEY (created_by)
                                          REFERENCES app_user (id) ON DELETE RESTRICT,
    CONSTRAINT chk_group_max              CHECK (max_members IS NULL OR max_members > 0)
);

CREATE UNIQUE INDEX uk_group_course_module_name ON course_group (course_id, COALESCE(module_id, -1), name);

COMMENT ON TABLE course_group IS 'Student groups for collaboration. Supports course-wide (module_id IS NULL) or module-specific (module_id IS NOT NULL) groups.';
COMMENT ON COLUMN course_group.module_id IS 'NULL = course-wide group. NOT NULL = module-specific group (e.g., "Module 5 Final Project - Group A").';
