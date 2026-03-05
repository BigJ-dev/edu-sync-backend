-- =============================================================================
-- EduSync - V36: All indexes (UPDATED for simplified schema)
-- UNIQUE constraints already create implicit indexes.
-- Partial indexes (WHERE) for sparse lookups.
-- =============================================================================

SET search_path TO edusync;

-- ===== IDENTITY =====
CREATE INDEX IF NOT EXISTS idx_student_email               ON student (email);
CREATE INDEX IF NOT EXISTS idx_student_blocked             ON student (active) WHERE active = FALSE;
CREATE INDEX IF NOT EXISTS idx_app_user_blocked            ON app_user (active) WHERE active = FALSE;

-- ===== COURSE =====
CREATE INDEX IF NOT EXISTS idx_course_lecturer             ON course (lecturer_id);
CREATE INDEX IF NOT EXISTS idx_course_status               ON course (status);

-- ===== MODULES =====
CREATE INDEX IF NOT EXISTS idx_module_course               ON course_module (course_id, sort_order);
CREATE INDEX IF NOT EXISTS idx_module_status               ON course_module (course_id, status);

-- ===== CATEGORIES =====
CREATE INDEX IF NOT EXISTS idx_category_parent             ON course_category (parent_id) WHERE parent_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_category_active             ON course_category (active, sort_order);

-- ===== ENROLLMENT =====
CREATE INDEX IF NOT EXISTS idx_enrollment_student          ON course_enrollment (student_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_status           ON course_enrollment (course_id, status);
CREATE INDEX IF NOT EXISTS idx_enrollment_blocked          ON course_enrollment (course_id, is_blocked) WHERE is_blocked = TRUE;

-- ===== CLASS SESSIONS =====
CREATE INDEX IF NOT EXISTS idx_session_module              ON class_session (module_id);
CREATE INDEX IF NOT EXISTS idx_session_type                ON class_session (session_type);
CREATE INDEX IF NOT EXISTS idx_session_teams_id            ON class_session (teams_meeting_id) WHERE teams_meeting_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_session_status_start        ON class_session (status, scheduled_start);

-- ===== ATTENDANCE =====
CREATE INDEX IF NOT EXISTS idx_attendance_student          ON attendance_record (student_id);
CREATE INDEX IF NOT EXISTS idx_attendance_course_student   ON attendance_record (course_id, student_id);
CREATE INDEX IF NOT EXISTS idx_attendance_module_student   ON attendance_record (module_id, student_id);
CREATE INDEX IF NOT EXISTS idx_attendance_report           ON attendance_record (report_id) WHERE report_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_att_log_record_time         ON attendance_log (attendance_record_id, event_time);

-- ===== STUDY MATERIALS =====
CREATE INDEX IF NOT EXISTS idx_material_module_order       ON study_material (module_id, sort_order);
CREATE INDEX IF NOT EXISTS idx_material_session            ON study_material (class_session_id) WHERE class_session_id IS NOT NULL;

-- ===== ASSESSMENTS =====
CREATE INDEX IF NOT EXISTS idx_assessment_module           ON assessment (module_id);
CREATE INDEX IF NOT EXISTS idx_assessment_due              ON assessment (module_id, due_date);
CREATE INDEX IF NOT EXISTS idx_rubric_assessment           ON rubric_criteria (assessment_id, sort_order);
CREATE INDEX IF NOT EXISTS idx_submission_status           ON assessment_submission (assessment_id, status)
    WHERE status IN ('SUBMITTED', 'RESUBMITTED');
CREATE INDEX IF NOT EXISTS idx_submission_graded_by        ON assessment_submission (graded_by)
    WHERE graded_by IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_rubric_grade_submission     ON rubric_grade (submission_id);

-- ===== QUIZZES =====
CREATE INDEX IF NOT EXISTS idx_quiz_module                 ON quiz (module_id);
CREATE INDEX IF NOT EXISTS idx_quiz_session                ON quiz (class_session_id) WHERE class_session_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_question_quiz_sort          ON quiz_question (quiz_id, sort_order);
CREATE INDEX IF NOT EXISTS idx_option_question_sort        ON quiz_question_option (question_id, sort_order);
CREATE INDEX IF NOT EXISTS idx_attempt_student_quiz        ON quiz_attempt (student_id, quiz_id, attempt_number);
CREATE INDEX IF NOT EXISTS idx_answer_manual_grading       ON quiz_answer (quiz_attempt_id)
    WHERE requires_manual_grading = TRUE AND marks_awarded = 0;
CREATE INDEX IF NOT EXISTS idx_answer_option_answer        ON quiz_answer_option (answer_id);

-- ===== MESSAGING (SIMPLIFIED SCHEMA) =====
CREATE INDEX IF NOT EXISTS idx_thread_course_student       ON message_thread (course_id, student_id);
CREATE INDEX IF NOT EXISTS idx_thread_course_last_msg      ON message_thread (course_id, last_message_at DESC);
CREATE INDEX IF NOT EXISTS idx_thread_open                 ON message_thread (status) WHERE status = 'OPEN';
CREATE INDEX IF NOT EXISTS idx_thread_escalated            ON message_thread (is_escalated, status) WHERE is_escalated = TRUE;
CREATE INDEX IF NOT EXISTS idx_thread_staff_unread         ON message_thread (course_id, last_message_at DESC)
    WHERE staff_unread_count > 0;
CREATE INDEX IF NOT EXISTS idx_thread_student_unread       ON message_thread (student_id, last_message_at DESC)
    WHERE student_unread_count > 0;
CREATE INDEX IF NOT EXISTS idx_thread_escalated_date       ON message_thread (escalated_at DESC)
    WHERE is_escalated = TRUE;

CREATE INDEX IF NOT EXISTS idx_message_thread_created      ON message (thread_id, created_at);
CREATE INDEX IF NOT EXISTS idx_message_sender              ON message (sender_type, sender_id) WHERE sender_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_message_not_deleted         ON message (thread_id, created_at) WHERE deleted_at IS NULL;

CREATE INDEX IF NOT EXISTS idx_read_receipt_reader         ON message_read_receipt (reader_type, reader_id);

-- ===== FOCUS MODE =====
CREATE INDEX IF NOT EXISTS idx_focus_active                ON lecturer_focus_mode (course_id, is_active) WHERE is_active = TRUE;
CREATE INDEX IF NOT EXISTS idx_focus_module_active         ON lecturer_focus_mode (module_id, is_active) WHERE is_active = TRUE AND module_id IS NOT NULL;

-- ===== BROADCASTS =====
CREATE INDEX IF NOT EXISTS idx_broadcast_course_sent       ON broadcast_message (course_id, sent_at DESC);
CREATE INDEX IF NOT EXISTS idx_broadcast_module_sent       ON broadcast_message (module_id, sent_at DESC) WHERE module_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_bcast_recip_student         ON broadcast_recipient (student_id);
CREATE INDEX IF NOT EXISTS idx_bcast_recip_unread          ON broadcast_recipient (student_id, read_at) WHERE read_at IS NULL;

-- ===== NOTIFICATIONS (SIMPLIFIED SCHEMA) =====
CREATE INDEX IF NOT EXISTS idx_notif_recipient_unread      ON notification (recipient_type, recipient_id, created_at DESC)
    WHERE read_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_notif_recipient_all         ON notification (recipient_type, recipient_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_notif_module                ON notification (module_id, created_at DESC) WHERE module_id IS NOT NULL;

-- ===== AUDIT LOG =====
CREATE INDEX IF NOT EXISTS idx_audit_entity                ON audit_log (entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_course                ON audit_log (course_id) WHERE course_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_audit_action_time           ON audit_log (action, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_audit_created               ON audit_log (created_at DESC);

-- ===== CERTIFICATES =====
CREATE INDEX IF NOT EXISTS idx_cert_student                ON certificate (student_id);
CREATE INDEX IF NOT EXISTS idx_cert_course                 ON certificate (course_id);
CREATE INDEX IF NOT EXISTS idx_cert_status                 ON certificate (status);

-- ===== GROUPS =====
CREATE INDEX IF NOT EXISTS idx_group_course                ON course_group (course_id);
CREATE INDEX IF NOT EXISTS idx_group_module                ON course_group (module_id) WHERE module_id IS NOT NULL;
CREATE INDEX IF NOT EXISTS idx_gm_student                  ON course_group_member (student_id);

-- ===== SYSTEM SETTINGS =====
CREATE INDEX IF NOT EXISTS idx_setting_category            ON system_setting (category);

-- ===== WEBHOOKS =====
CREATE INDEX IF NOT EXISTS idx_webhook_unprocessed         ON teams_webhook_event (processed, created_at) WHERE processed = FALSE;
