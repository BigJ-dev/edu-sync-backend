-- =============================================================================
-- EduSync - V37: Trigger function for automatic updated_at maintenance
-- =============================================================================

SET search_path TO edusync;

-- Create reusable trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

COMMENT ON FUNCTION update_updated_at_column() IS 'Automatically updates the updated_at timestamp on row modification';

-- Apply trigger to all tables with updated_at column
CREATE TRIGGER trg_app_user_updated_at
    BEFORE UPDATE ON app_user
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_student_updated_at
    BEFORE UPDATE ON student
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_course_category_updated_at
    BEFORE UPDATE ON course_category
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_course_updated_at
    BEFORE UPDATE ON course
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_course_module_updated_at
    BEFORE UPDATE ON course_module
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_class_session_updated_at
    BEFORE UPDATE ON class_session
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_attendance_record_updated_at
    BEFORE UPDATE ON attendance_record
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_study_material_updated_at
    BEFORE UPDATE ON study_material
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_assessment_updated_at
    BEFORE UPDATE ON assessment
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_assessment_submission_updated_at
    BEFORE UPDATE ON assessment_submission
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_quiz_updated_at
    BEFORE UPDATE ON quiz
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_quiz_question_updated_at
    BEFORE UPDATE ON quiz_question
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_quiz_question_option_updated_at
    BEFORE UPDATE ON quiz_question_option
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_quiz_attempt_updated_at
    BEFORE UPDATE ON quiz_attempt
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_message_thread_updated_at
    BEFORE UPDATE ON message_thread
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_broadcast_message_updated_at
    BEFORE UPDATE ON broadcast_message
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_certificate_updated_at
    BEFORE UPDATE ON certificate
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_system_setting_updated_at
    BEFORE UPDATE ON system_setting
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER trg_read_receipt_updated_at
    BEFORE UPDATE ON message_read_receipt
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Trigger for teams_attendance_report moved to V41 (table created there)
