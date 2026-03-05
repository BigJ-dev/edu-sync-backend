-- =============================================================================
-- EduSync - V4: course_category
-- Hierarchical categories via self-referencing parent_id.
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS course_category (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid        UUID           NOT NULL DEFAULT gen_random_uuid(),
    name        VARCHAR(100)   NOT NULL,
    description TEXT,
    parent_id   BIGINT,
    sort_order  INT            NOT NULL DEFAULT 0,
    icon_name   VARCHAR(50),
    active      BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_category_uuid        UNIQUE (uuid),
    CONSTRAINT uk_category_name_parent UNIQUE (name, parent_id),
    CONSTRAINT fk_category_parent      FOREIGN KEY (parent_id)
                                       REFERENCES course_category (id) ON DELETE SET NULL,
    CONSTRAINT chk_category_no_self    CHECK (parent_id != id)
);
