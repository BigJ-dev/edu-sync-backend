-- =============================================================================
-- EduSync - V7: course_category_mapping (M:M course <-> category)
-- =============================================================================

SET search_path TO edusync;

CREATE TABLE IF NOT EXISTS course_category_mapping (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    course_id     BIGINT  NOT NULL,
    category_id   BIGINT  NOT NULL,

    CONSTRAINT uk_course_category  UNIQUE (course_id, category_id),
    CONSTRAINT fk_ccm_course       FOREIGN KEY (course_id)
                                   REFERENCES course (id) ON DELETE CASCADE,
    CONSTRAINT fk_ccm_category     FOREIGN KEY (category_id)
                                   REFERENCES course_category (id) ON DELETE CASCADE
);
