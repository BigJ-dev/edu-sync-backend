-- =============================================================================
-- EduSync - V35: Seed default system settings
-- =============================================================================

SET search_path TO edusync;

INSERT INTO system_setting (setting_key, setting_value, description, category) VALUES
    ('default_min_attendance_pct',    '80',        'Default minimum attendance % for new courses',                 'ATTENDANCE'),
    ('attendance_present_threshold',  '75',        'Min % of session duration to count as PRESENT vs PARTIAL',    'ATTENDANCE'),
    ('attendance_partial_threshold',  '5',         'Min minutes in meeting to count as PARTIAL vs ABSENT',        'ATTENDANCE'),
    ('max_file_upload_bytes',         '104857600', 'Max file upload size (100MB)',                                 'STORAGE'),
    ('allowed_upload_mime_types',     'application/pdf,image/png,image/jpeg,video/mp4,application/vnd.openxmlformats-officedocument.presentationml.presentation,application/vnd.openxmlformats-officedocument.wordprocessingml.document', 'Allowed MIME types', 'STORAGE'),
    ('presigned_url_ttl_minutes',     '15',        'S3 presigned URL expiry in minutes',                          'STORAGE'),
    ('session_reminder_minutes',      '15',        'Minutes before session to send reminder',                     'NOTIFICATION'),
    ('assessment_due_reminder_hours', '24',        'Hours before due date to send reminder',                      'NOTIFICATION'),
    ('focus_mode_max_hours',          '4',         'Max focus mode duration if no scheduled_end',                 'MESSAGING'),
    ('quiz_attempt_grace_minutes',    '5',         'Extra minutes after time limit before auto-submit',           'QUIZ'),
    ('certificate_issuer_name',       'EduSync Learning Platform', 'Organization name on certificates',           'CERTIFICATE'),
    ('certificate_number_prefix',     'EDUSYNC',   'Prefix for certificate numbers',                              'CERTIFICATE'),
    ('maintenance_mode',              'false',     'When true, non-admin requests return 503',                    'SYSTEM'),
    ('platform_name',                 'EduSync',   'Platform display name',                                       'SYSTEM')
ON CONFLICT (setting_key) DO UPDATE
    SET setting_value = EXCLUDED.setting_value,
        description   = EXCLUDED.description,
        category      = EXCLUDED.category,
        updated_at    = NOW();
