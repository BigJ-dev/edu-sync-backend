-- =============================================================================
-- EduSync - V40: Attendance denormalization validation trigger
-- Validates that course_id and module_id in attendance_record match
-- the class_session's module's course hierarchy
-- =============================================================================

SET search_path TO edusync;

-- =============================================================================
-- Trigger: Validate denormalized course_id and module_id
-- =============================================================================

CREATE OR REPLACE FUNCTION validate_attendance_denormalization()
RETURNS TRIGGER AS $$
DECLARE
    expected_module_id BIGINT;
    expected_course_id BIGINT;
BEGIN
    -- Get the module_id and course_id from the class_session
    SELECT cs.module_id, cm.course_id
    INTO expected_module_id, expected_course_id
    FROM class_session cs
    JOIN course_module cm ON cm.id = cs.module_id
    WHERE cs.id = NEW.class_session_id;

    -- Validate module_id matches
    IF NEW.module_id != expected_module_id THEN
        RAISE EXCEPTION 'attendance_record.module_id (%) does not match class_session.module_id (%)',
            NEW.module_id, expected_module_id;
    END IF;

    -- Validate course_id matches
    IF NEW.course_id != expected_course_id THEN
        RAISE EXCEPTION 'attendance_record.course_id (%) does not match module.course_id (%)',
            NEW.course_id, expected_course_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_attendance_denormalization
    BEFORE INSERT OR UPDATE ON attendance_record
    FOR EACH ROW
    EXECUTE FUNCTION validate_attendance_denormalization();

COMMENT ON FUNCTION validate_attendance_denormalization() IS 'Ensures denormalized course_id and module_id in attendance_record match class_session hierarchy. Prevents data inconsistency.';

-- =============================================================================
-- Helper function: Auto-populate course_id and module_id from class_session
-- Useful for application layer to call before insert
-- =============================================================================

CREATE OR REPLACE FUNCTION get_session_hierarchy(p_class_session_id BIGINT)
RETURNS TABLE(module_id BIGINT, course_id BIGINT) AS $$
BEGIN
    RETURN QUERY
    SELECT cs.module_id, cm.course_id
    FROM class_session cs
    JOIN course_module cm ON cm.id = cs.module_id
    WHERE cs.id = p_class_session_id;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION get_session_hierarchy(BIGINT) IS 'Helper function: Returns (module_id, course_id) for a class_session. Use when creating attendance_record.';
