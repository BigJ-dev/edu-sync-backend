-- =============================================================================
-- EduSync - V32: course_group_member
-- =============================================================================

SET search_path TO edusync;

DO $$ BEGIN
    CREATE TYPE group_member_role AS ENUM ('LEADER', 'MEMBER');
EXCEPTION WHEN duplicate_object THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS course_group_member (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    group_id    BIGINT            NOT NULL,
    student_id  BIGINT            NOT NULL,
    role        group_member_role NOT NULL DEFAULT 'MEMBER',
    joined_at   TIMESTAMPTZ       NOT NULL DEFAULT NOW(),

    CONSTRAINT uk_group_member UNIQUE (group_id, student_id),
    CONSTRAINT fk_gm_group     FOREIGN KEY (group_id)
                                REFERENCES course_group (id) ON DELETE CASCADE,
    CONSTRAINT fk_gm_student   FOREIGN KEY (student_id)
                                REFERENCES student (id) ON DELETE CASCADE
);
