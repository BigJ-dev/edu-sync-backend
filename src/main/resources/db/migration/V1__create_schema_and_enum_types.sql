-- =============================================================================
-- EduSync Learning Management Platform
-- V1: Create schema and all enum types
-- =============================================================================

CREATE SCHEMA IF NOT EXISTS edusync;
SET search_path TO edusync;

-- ===== Identity =====
CREATE TYPE user_role             AS ENUM ('ADMIN', 'LECTURER');

-- ===== Course & Module Lifecycle =====
CREATE TYPE course_status         AS ENUM ('DRAFT', 'PUBLISHED', 'IN_PROGRESS', 'COMPLETED', 'ARCHIVED');
CREATE TYPE module_status         AS ENUM ('DRAFT', 'PUBLISHED', 'LOCKED', 'COMPLETED');
CREATE TYPE enrollment_status     AS ENUM ('ENROLLED', 'WITHDRAWN', 'COMPLETED', 'FAILED');

-- ===== Sessions & Attendance =====
CREATE TYPE session_type          AS ENUM ('ONLINE', 'PHYSICAL');
CREATE TYPE session_status        AS ENUM ('SCHEDULED', 'LIVE', 'COMPLETED', 'CANCELLED');
CREATE TYPE attendance_status     AS ENUM ('PRESENT', 'PARTIAL', 'ABSENT');
CREATE TYPE attendance_event      AS ENUM ('JOIN', 'LEAVE');
CREATE TYPE report_sync_status   AS ENUM ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED');

-- ===== Content =====
CREATE TYPE material_type         AS ENUM ('DOCUMENT', 'VIDEO', 'LINK', 'SLIDE_DECK', 'IMAGE', 'OTHER');
CREATE TYPE delivery_mode         AS ENUM ('ONLINE', 'PHYSICAL');
CREATE TYPE assessment_type       AS ENUM ('ASSIGNMENT', 'PROJECT', 'PRACTICAL', 'PRESENTATION', 'OTHER');
CREATE TYPE assessment_status     AS ENUM ('DRAFT', 'PUBLISHED', 'CLOSED', 'GRADED');
CREATE TYPE submission_status     AS ENUM ('SUBMITTED', 'GRADED', 'RETURNED', 'RESUBMITTED');
CREATE TYPE quiz_status           AS ENUM ('DRAFT', 'PUBLISHED', 'CLOSED');
CREATE TYPE question_type         AS ENUM ('MULTIPLE_CHOICE', 'MULTI_SELECT', 'TRUE_FALSE', 'SHORT_ANSWER');
CREATE TYPE quiz_attempt_status   AS ENUM ('IN_PROGRESS', 'COMPLETED', 'TIMED_OUT');

-- ===== Messaging =====
CREATE TYPE thread_status         AS ENUM ('OPEN', 'RESOLVED', 'CLOSED');
CREATE TYPE thread_priority       AS ENUM ('NORMAL', 'URGENT');
CREATE TYPE sender_type           AS ENUM ('STUDENT', 'LECTURER', 'ADMIN');
CREATE TYPE broadcast_target      AS ENUM ('ALL_COURSE', 'SPECIFIC_MODULE', 'SPECIFIC_SESSION', 'SPECIFIC_STUDENTS', 'GLOBAL');
CREATE TYPE broadcast_priority    AS ENUM ('LOW', 'NORMAL', 'URGENT');

-- ===== Platform =====
CREATE TYPE notification_type AS ENUM (
    'SESSION_SCHEDULED', 'SESSION_STARTING', 'SESSION_RECORDING_READY',
    'ASSESSMENT_PUBLISHED', 'ASSESSMENT_GRADED', 'ASSESSMENT_DUE_REMINDER',
    'QUIZ_PUBLISHED', 'QUIZ_GRADED',
    'MATERIAL_UPLOADED', 'MODULE_PUBLISHED',
    'MESSAGE_RECEIVED', 'THREAD_ESCALATED',
    'BROADCAST_RECEIVED',
    'ENROLLMENT_CONFIRMED', 'CERTIFICATE_ISSUED', 'COURSE_COMPLETED',
    'ATTENDANCE_FLAGGED'
);

CREATE TYPE audit_action AS ENUM (
    'CREATE', 'UPDATE', 'DELETE', 'STATUS_CHANGE',
    'GRADE_ASSIGNED', 'GRADE_CHANGED',
    'ATTENDANCE_OVERRIDE', 'ENROLLMENT_CHANGE',
    'ESCALATION', 'FOCUS_MODE_TOGGLE',
    'CERTIFICATE_ISSUED', 'PERMISSION_CHANGE'
);

CREATE TYPE certificate_status    AS ENUM ('PENDING', 'ISSUED', 'REVOKED');
CREATE TYPE group_member_role     AS ENUM ('LEADER', 'MEMBER');
