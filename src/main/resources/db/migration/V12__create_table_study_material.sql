-- =============================================================================
-- EduSync - V12: study_material
-- Belongs to a MODULE. Optionally linked to a specific session.
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE material_type AS ENUM ('DOCUMENT', 'VIDEO', 'LINK', 'SLIDE_DECK', 'IMAGE', 'OTHER');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS study_material (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    uuid                  UUID           NOT NULL DEFAULT gen_random_uuid(),
    module_id             BIGINT         NOT NULL,
    class_session_id      BIGINT,
    uploaded_by           BIGINT         NOT NULL,
    title                 VARCHAR(255)   NOT NULL,
    description           TEXT,
    material_type         material_type  NOT NULL,
    s3_key                VARCHAR(500),
    external_url          VARCHAR(1000),
    file_name             VARCHAR(255),
    file_size_bytes       BIGINT,
    mime_type             VARCHAR(100),
    sort_order            INT            NOT NULL DEFAULT 0,
    visible_to_students   BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at            TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at            TIMESTAMPTZ    NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_material_uuid        UNIQUE (uuid),
    CONSTRAINT fk_material_module      FOREIGN KEY (module_id)
                                       REFERENCES course_module (id) ON DELETE CASCADE,
    CONSTRAINT fk_material_session     FOREIGN KEY (class_session_id)
                                       REFERENCES class_session (id) ON DELETE SET NULL,
    CONSTRAINT fk_material_uploaded_by FOREIGN KEY (uploaded_by)
                                       REFERENCES app_user (id) ON DELETE RESTRICT,
    CONSTRAINT chk_material_source     CHECK (
        (material_type = 'LINK' AND external_url IS NOT NULL)
        OR (material_type != 'LINK' AND s3_key IS NOT NULL)
    ),
    CONSTRAINT chk_material_file_size  CHECK (file_size_bytes IS NULL OR file_size_bytes > 0)
);

COMMENT ON COLUMN study_material.s3_key IS 'S3: materials/{module_uuid}/{uuid}/{file_name}';
