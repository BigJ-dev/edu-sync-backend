package com.edusync.api.common.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidationMessage {

    COGNITO_SUB_REQUIRED("Cognito subject is required"),
    EMAIL_REQUIRED("Email is required"),
    EMAIL_INVALID("Email must be valid"),
    FIRST_NAME_REQUIRED("First name is required"),
    FIRST_NAME_SIZE("First name must not exceed 100 characters"),
    LAST_NAME_REQUIRED("Last name is required"),
    LAST_NAME_SIZE("Last name must not exceed 100 characters"),
    STUDENT_NUMBER_REQUIRED("Student number is required"),
    STUDENT_NUMBER_SIZE("Student number must not exceed 50 characters"),
    PHONE_SIZE("Phone must not exceed 20 characters"),
    BLOCKED_REASON_REQUIRED("Blocked reason is required"),
    BLOCKED_REASON_SIZE("Blocked reason must not exceed 500 characters"),
    PROFILE_IMAGE_KEY_REQUIRED("Profile image key is required"),
    PROFILE_IMAGE_KEY_SIZE("Profile image key must not exceed 500 characters"),

    // Course
    COURSE_CODE_REQUIRED("Course code is required"),
    COURSE_CODE_SIZE("Course code must not exceed 20 characters"),
    COURSE_TITLE_REQUIRED("Course title is required"),
    COURSE_TITLE_SIZE("Course title must not exceed 255 characters"),
    COURSE_LECTURER_REQUIRED("Lecturer is required"),
    COURSE_START_DATE_REQUIRED("Start date is required"),
    COURSE_END_DATE_REQUIRED("End date is required"),
    COURSE_STATUS_REQUIRED("Course status is required"),
    COURSE_THUMBNAIL_SIZE("Thumbnail key must not exceed 500 characters"),
    COURSE_DESCRIPTION_SIZE("Course description must not exceed 5000 characters"),
    COURSE_MIN_ATTENDANCE_RANGE("Minimum attendance must be between 0 and 100"),

    // Module
    MODULE_TITLE_REQUIRED("Module title is required"),
    MODULE_TITLE_SIZE("Module title must not exceed 255 characters"),
    MODULE_SORT_ORDER_REQUIRED("Sort order is required"),
    MODULE_SORT_ORDER_MIN("Sort order must be 0 or greater"),
    MODULE_STATUS_REQUIRED("Module status is required"),
    MODULE_DESCRIPTION_SIZE("Module description must not exceed 5000 characters"),

    // Enrollment
    ENROLLMENT_STUDENT_REQUIRED("Student is required"),
    ENROLLMENT_WITHDRAWAL_REASON_REQUIRED("Withdrawal reason is required"),
    ENROLLMENT_WITHDRAWAL_REASON_SIZE("Withdrawal reason must not exceed 500 characters"),

    // Session
    SESSION_TITLE_REQUIRED("Session title is required"),
    SESSION_TITLE_SIZE("Session title must not exceed 255 characters"),
    SESSION_DESCRIPTION_SIZE("Session description must not exceed 5000 characters"),
    SESSION_TYPE_REQUIRED("Session type is required"),
    SESSION_NUMBER_REQUIRED("Session number is required"),
    SESSION_NUMBER_MIN("Session number must be 1 or greater"),
    SESSION_SCHEDULED_START_REQUIRED("Scheduled start time is required"),
    SESSION_SCHEDULED_END_REQUIRED("Scheduled end time is required"),
    SESSION_STATUS_REQUIRED("Session status is required"),
    SESSION_VENUE_SIZE("Venue must not exceed 500 characters"),
    SESSION_LECTURER_REQUIRED("Lecturer is required"),

    // Attendance
    ATTENDANCE_STUDENT_REQUIRED("Student is required"),
    ATTENDANCE_STATUS_REQUIRED("Attendance status is required"),
    ATTENDANCE_OVERRIDE_REASON_REQUIRED("Override reason is required"),
    ATTENDANCE_OVERRIDE_REASON_SIZE("Override reason must not exceed 500 characters"),

    // Study Material
    MATERIAL_TITLE_REQUIRED("Material title is required"),
    MATERIAL_TITLE_SIZE("Material title must not exceed 255 characters"),
    MATERIAL_DESCRIPTION_SIZE("Material description must not exceed 5000 characters"),
    MATERIAL_TYPE_REQUIRED("Material type is required"),
    MATERIAL_UPLOADED_BY_REQUIRED("Uploader is required"),
    MATERIAL_S3_KEY_SIZE("S3 key must not exceed 500 characters"),
    MATERIAL_EXTERNAL_URL_SIZE("External URL must not exceed 1000 characters"),
    MATERIAL_FILE_NAME_SIZE("File name must not exceed 255 characters"),
    MATERIAL_MIME_TYPE_SIZE("MIME type must not exceed 100 characters"),

    // Assessment
    ASSESSMENT_TITLE_REQUIRED("Assessment title is required"),
    ASSESSMENT_TITLE_SIZE("Assessment title must not exceed 255 characters"),
    ASSESSMENT_DESCRIPTION_SIZE("Assessment description must not exceed 5000 characters"),
    ASSESSMENT_TYPE_REQUIRED("Assessment type is required"),
    ASSESSMENT_DELIVERY_MODE_REQUIRED("Delivery mode is required"),
    ASSESSMENT_TOTAL_MARKS_REQUIRED("Total marks is required"),
    ASSESSMENT_TOTAL_MARKS_MIN("Total marks must be greater than 0"),
    ASSESSMENT_WEIGHT_RANGE("Weight percentage must be between 0 and 100"),
    ASSESSMENT_DUE_DATE_REQUIRED("Due date is required"),
    ASSESSMENT_STATUS_REQUIRED("Assessment status is required"),
    ASSESSMENT_CREATED_BY_REQUIRED("Creator is required"),
    ASSESSMENT_PENALTY_RANGE("Late penalty percentage must be between 0 and 100"),
    ASSESSMENT_BRIEF_S3_KEY_SIZE("Brief S3 key must not exceed 500 characters"),
    ASSESSMENT_BRIEF_FILE_NAME_SIZE("Brief file name must not exceed 255 characters"),

    // Rubric Criteria
    RUBRIC_TITLE_REQUIRED("Rubric criteria title is required"),
    RUBRIC_TITLE_SIZE("Rubric criteria title must not exceed 255 characters"),
    RUBRIC_DESCRIPTION_SIZE("Rubric criteria description must not exceed 5000 characters"),
    RUBRIC_MAX_MARKS_REQUIRED("Maximum marks is required"),
    RUBRIC_MAX_MARKS_MIN("Maximum marks must be greater than 0"),
    RUBRIC_SORT_ORDER_REQUIRED("Sort order is required"),
    RUBRIC_SORT_ORDER_MIN("Sort order must be 0 or greater"),

    // Assessment Submission
    SUBMISSION_STUDENT_REQUIRED("Student is required"),
    SUBMISSION_S3_KEY_SIZE("S3 key must not exceed 500 characters"),
    SUBMISSION_FILE_NAME_SIZE("File name must not exceed 255 characters"),
    SUBMISSION_MIME_TYPE_SIZE("MIME type must not exceed 100 characters"),
    SUBMISSION_FEEDBACK_SIZE("Feedback must not exceed 5000 characters"),
    SUBMISSION_MARKS_MIN("Marks must be 0 or greater"),
    SUBMISSION_STATUS_REQUIRED("Submission status is required"),

    // Rubric Grade
    RUBRIC_GRADE_MARKS_REQUIRED("Marks awarded is required"),
    RUBRIC_GRADE_MARKS_MIN("Marks awarded must be 0 or greater"),
    RUBRIC_GRADE_COMMENT_SIZE("Comment must not exceed 5000 characters"),
    RUBRIC_GRADE_GRADED_BY_REQUIRED("Grader is required"),

    // Broadcast
    BROADCAST_TITLE_REQUIRED("Broadcast title is required"),
    BROADCAST_TITLE_SIZE("Broadcast title must not exceed 255 characters"),
    BROADCAST_BODY_REQUIRED("Broadcast body is required"),
    BROADCAST_SENT_BY_REQUIRED("Sender is required"),
    BROADCAST_TARGET_REQUIRED("Target type is required"),
    BROADCAST_PRIORITY_REQUIRED("Priority is required"),
    BROADCAST_ATTACHMENT_S3_KEY_SIZE("Attachment S3 key must not exceed 500 characters"),
    BROADCAST_ATTACHMENT_NAME_SIZE("Attachment name must not exceed 255 characters"),

    // Message Thread
    THREAD_COURSE_REQUIRED("Course is required"),
    THREAD_STUDENT_REQUIRED("Student is required"),
    THREAD_SUBJECT_REQUIRED("Subject is required"),
    THREAD_SUBJECT_SIZE("Subject must not exceed 255 characters"),
    THREAD_STATUS_REQUIRED("Thread status is required"),
    THREAD_BODY_REQUIRED("Message body is required"),

    // Message
    MESSAGE_SENDER_TYPE_REQUIRED("Sender type is required"),
    MESSAGE_SENDER_REQUIRED("Sender is required"),
    MESSAGE_BODY_REQUIRED("Message body is required"),
    MESSAGE_ATTACHMENT_S3_KEY_SIZE("Attachment S3 key must not exceed 500 characters"),
    MESSAGE_ATTACHMENT_NAME_SIZE("Attachment name must not exceed 255 characters"),
    MESSAGE_READER_TYPE_REQUIRED("Reader type is required"),
    MESSAGE_READER_REQUIRED("Reader is required"),

    // Notification
    NOTIFICATION_RECIPIENT_TYPE_REQUIRED("Recipient type is required"),
    NOTIFICATION_RECIPIENT_ID_REQUIRED("Recipient ID is required"),
    NOTIFICATION_TYPE_REQUIRED("Notification type is required"),
    NOTIFICATION_TITLE_REQUIRED("Notification title is required"),
    NOTIFICATION_TITLE_SIZE("Notification title must not exceed 255 characters"),

    // Quiz
    QUIZ_TITLE_REQUIRED("Quiz title is required"),
    QUIZ_TITLE_SIZE("Quiz title must not exceed 255 characters"),
    QUIZ_DESCRIPTION_SIZE("Quiz description must not exceed 5000 characters"),
    QUIZ_CREATED_BY_REQUIRED("Creator is required"),
    QUIZ_TOTAL_MARKS_REQUIRED("Total marks is required"),
    QUIZ_TOTAL_MARKS_MIN("Total marks must be greater than 0"),
    QUIZ_PASS_MARK_RANGE("Pass mark percentage must be between 0 and 100"),
    QUIZ_WEIGHT_RANGE("Weight percentage must be between 0 and 100"),
    QUIZ_MAX_ATTEMPTS_MIN("Maximum attempts must be at least 1"),
    QUIZ_TIME_LIMIT_MIN("Time limit must be at least 1 minute"),
    QUIZ_STATUS_REQUIRED("Quiz status is required"),
    QUIZ_DOCUMENT_S3_KEY_SIZE("Document S3 key must not exceed 500 characters"),
    QUIZ_DOCUMENT_NAME_SIZE("Document name must not exceed 255 characters"),

    // Quiz Question
    QUESTION_TEXT_REQUIRED("Question text is required"),
    QUESTION_TYPE_REQUIRED("Question type is required"),
    QUESTION_MARKS_REQUIRED("Question marks is required"),
    QUESTION_MARKS_MIN("Question marks must be greater than 0"),
    QUESTION_SORT_ORDER_REQUIRED("Sort order is required"),
    QUESTION_SORT_ORDER_MIN("Sort order must be 0 or greater"),
    QUESTION_IMAGE_S3_KEY_SIZE("Image S3 key must not exceed 500 characters"),

    // Quiz Question Option
    OPTION_TEXT_REQUIRED("Option text is required"),
    OPTION_TEXT_SIZE("Option text must not exceed 1000 characters"),
    OPTION_SORT_ORDER_REQUIRED("Sort order is required"),
    OPTION_SORT_ORDER_MIN("Sort order must be 0 or greater"),

    // Quiz Attempt
    ATTEMPT_STUDENT_REQUIRED("Student is required"),
    ATTEMPT_QUESTION_REQUIRED("Question is required"),
    ATTEMPT_ANSWER_TEXT_SIZE("Answer text must not exceed 2000 characters");

    private final String message;

    // Actor
    public static final String _COGNITO_SUB_REQUIRED = "Cognito subject is required";
    public static final String _EMAIL_REQUIRED = "Email is required";
    public static final String _EMAIL_INVALID = "Email must be valid";
    public static final String _FIRST_NAME_REQUIRED = "First name is required";
    public static final String _FIRST_NAME_SIZE = "First name must not exceed 100 characters";
    public static final String _LAST_NAME_REQUIRED = "Last name is required";
    public static final String _LAST_NAME_SIZE = "Last name must not exceed 100 characters";
    public static final String _STUDENT_NUMBER_REQUIRED = "Student number is required";
    public static final String _STUDENT_NUMBER_SIZE = "Student number must not exceed 50 characters";
    public static final String _PHONE_SIZE = "Phone must not exceed 20 characters";
    public static final String _BLOCKED_REASON_REQUIRED = "Blocked reason is required";
    public static final String _BLOCKED_REASON_SIZE = "Blocked reason must not exceed 500 characters";
    public static final String _PROFILE_IMAGE_KEY_REQUIRED = "Profile image key is required";
    public static final String _PROFILE_IMAGE_KEY_SIZE = "Profile image key must not exceed 500 characters";

    // Course
    public static final String _COURSE_CODE_REQUIRED = "Course code is required";
    public static final String _COURSE_CODE_SIZE = "Course code must not exceed 20 characters";
    public static final String _COURSE_TITLE_REQUIRED = "Course title is required";
    public static final String _COURSE_TITLE_SIZE = "Course title must not exceed 255 characters";
    public static final String _COURSE_LECTURER_REQUIRED = "Lecturer is required";
    public static final String _COURSE_START_DATE_REQUIRED = "Start date is required";
    public static final String _COURSE_END_DATE_REQUIRED = "End date is required";
    public static final String _COURSE_STATUS_REQUIRED = "Course status is required";
    public static final String _COURSE_THUMBNAIL_SIZE = "Thumbnail key must not exceed 500 characters";
    public static final String _COURSE_DESCRIPTION_SIZE = "Course description must not exceed 5000 characters";
    public static final String _COURSE_MIN_ATTENDANCE_RANGE = "Minimum attendance must be between 0 and 100";

    // Module
    public static final String _MODULE_TITLE_REQUIRED = "Module title is required";
    public static final String _MODULE_TITLE_SIZE = "Module title must not exceed 255 characters";
    public static final String _MODULE_SORT_ORDER_REQUIRED = "Sort order is required";
    public static final String _MODULE_SORT_ORDER_MIN = "Sort order must be 0 or greater";
    public static final String _MODULE_STATUS_REQUIRED = "Module status is required";
    public static final String _MODULE_DESCRIPTION_SIZE = "Module description must not exceed 5000 characters";

    // Enrollment
    public static final String _ENROLLMENT_STUDENT_REQUIRED = "Student is required";
    public static final String _ENROLLMENT_WITHDRAWAL_REASON_REQUIRED = "Withdrawal reason is required";
    public static final String _ENROLLMENT_WITHDRAWAL_REASON_SIZE = "Withdrawal reason must not exceed 500 characters";

    // Session
    public static final String _SESSION_TITLE_REQUIRED = "Session title is required";
    public static final String _SESSION_TITLE_SIZE = "Session title must not exceed 255 characters";
    public static final String _SESSION_DESCRIPTION_SIZE = "Session description must not exceed 5000 characters";
    public static final String _SESSION_TYPE_REQUIRED = "Session type is required";
    public static final String _SESSION_NUMBER_REQUIRED = "Session number is required";
    public static final String _SESSION_NUMBER_MIN = "Session number must be 1 or greater";
    public static final String _SESSION_SCHEDULED_START_REQUIRED = "Scheduled start time is required";
    public static final String _SESSION_SCHEDULED_END_REQUIRED = "Scheduled end time is required";
    public static final String _SESSION_STATUS_REQUIRED = "Session status is required";
    public static final String _SESSION_VENUE_SIZE = "Venue must not exceed 500 characters";
    public static final String _SESSION_LECTURER_REQUIRED = "Lecturer is required";

    // Attendance
    public static final String _ATTENDANCE_STUDENT_REQUIRED = "Student is required";
    public static final String _ATTENDANCE_STATUS_REQUIRED = "Attendance status is required";
    public static final String _ATTENDANCE_OVERRIDE_REASON_REQUIRED = "Override reason is required";
    public static final String _ATTENDANCE_OVERRIDE_REASON_SIZE = "Override reason must not exceed 500 characters";

    // Study Material
    public static final String _MATERIAL_TITLE_REQUIRED = "Material title is required";
    public static final String _MATERIAL_TITLE_SIZE = "Material title must not exceed 255 characters";
    public static final String _MATERIAL_DESCRIPTION_SIZE = "Material description must not exceed 5000 characters";
    public static final String _MATERIAL_TYPE_REQUIRED = "Material type is required";
    public static final String _MATERIAL_UPLOADED_BY_REQUIRED = "Uploader is required";
    public static final String _MATERIAL_S3_KEY_SIZE = "S3 key must not exceed 500 characters";
    public static final String _MATERIAL_EXTERNAL_URL_SIZE = "External URL must not exceed 1000 characters";
    public static final String _MATERIAL_FILE_NAME_SIZE = "File name must not exceed 255 characters";
    public static final String _MATERIAL_MIME_TYPE_SIZE = "MIME type must not exceed 100 characters";

    // Assessment
    public static final String _ASSESSMENT_TITLE_REQUIRED = "Assessment title is required";
    public static final String _ASSESSMENT_TITLE_SIZE = "Assessment title must not exceed 255 characters";
    public static final String _ASSESSMENT_DESCRIPTION_SIZE = "Assessment description must not exceed 5000 characters";
    public static final String _ASSESSMENT_TYPE_REQUIRED = "Assessment type is required";
    public static final String _ASSESSMENT_DELIVERY_MODE_REQUIRED = "Delivery mode is required";
    public static final String _ASSESSMENT_TOTAL_MARKS_REQUIRED = "Total marks is required";
    public static final String _ASSESSMENT_TOTAL_MARKS_MIN = "Total marks must be greater than 0";
    public static final String _ASSESSMENT_WEIGHT_RANGE = "Weight percentage must be between 0 and 100";
    public static final String _ASSESSMENT_DUE_DATE_REQUIRED = "Due date is required";
    public static final String _ASSESSMENT_STATUS_REQUIRED = "Assessment status is required";
    public static final String _ASSESSMENT_CREATED_BY_REQUIRED = "Creator is required";
    public static final String _ASSESSMENT_PENALTY_RANGE = "Late penalty percentage must be between 0 and 100";
    public static final String _ASSESSMENT_BRIEF_S3_KEY_SIZE = "Brief S3 key must not exceed 500 characters";
    public static final String _ASSESSMENT_BRIEF_FILE_NAME_SIZE = "Brief file name must not exceed 255 characters";

    // Rubric Criteria
    public static final String _RUBRIC_TITLE_REQUIRED = "Rubric criteria title is required";
    public static final String _RUBRIC_TITLE_SIZE = "Rubric criteria title must not exceed 255 characters";
    public static final String _RUBRIC_DESCRIPTION_SIZE = "Rubric criteria description must not exceed 5000 characters";
    public static final String _RUBRIC_MAX_MARKS_REQUIRED = "Maximum marks is required";
    public static final String _RUBRIC_MAX_MARKS_MIN = "Maximum marks must be greater than 0";
    public static final String _RUBRIC_SORT_ORDER_REQUIRED = "Sort order is required";
    public static final String _RUBRIC_SORT_ORDER_MIN = "Sort order must be 0 or greater";

    // Assessment Submission
    public static final String _SUBMISSION_STUDENT_REQUIRED = "Student is required";
    public static final String _SUBMISSION_S3_KEY_SIZE = "S3 key must not exceed 500 characters";
    public static final String _SUBMISSION_FILE_NAME_SIZE = "File name must not exceed 255 characters";
    public static final String _SUBMISSION_MIME_TYPE_SIZE = "MIME type must not exceed 100 characters";
    public static final String _SUBMISSION_FEEDBACK_SIZE = "Feedback must not exceed 5000 characters";
    public static final String _SUBMISSION_MARKS_MIN = "Marks must be 0 or greater";
    public static final String _SUBMISSION_STATUS_REQUIRED = "Submission status is required";

    // Rubric Grade
    public static final String _RUBRIC_GRADE_MARKS_REQUIRED = "Marks awarded is required";
    public static final String _RUBRIC_GRADE_MARKS_MIN = "Marks awarded must be 0 or greater";
    public static final String _RUBRIC_GRADE_COMMENT_SIZE = "Comment must not exceed 5000 characters";
    public static final String _RUBRIC_GRADE_GRADED_BY_REQUIRED = "Grader is required";

    // Broadcast
    public static final String _BROADCAST_TITLE_REQUIRED = "Broadcast title is required";
    public static final String _BROADCAST_TITLE_SIZE = "Broadcast title must not exceed 255 characters";
    public static final String _BROADCAST_BODY_REQUIRED = "Broadcast body is required";
    public static final String _BROADCAST_SENT_BY_REQUIRED = "Sender is required";
    public static final String _BROADCAST_TARGET_REQUIRED = "Target type is required";
    public static final String _BROADCAST_PRIORITY_REQUIRED = "Priority is required";
    public static final String _BROADCAST_ATTACHMENT_S3_KEY_SIZE = "Attachment S3 key must not exceed 500 characters";
    public static final String _BROADCAST_ATTACHMENT_NAME_SIZE = "Attachment name must not exceed 255 characters";

    // Message Thread
    public static final String _THREAD_COURSE_REQUIRED = "Course is required";
    public static final String _THREAD_STUDENT_REQUIRED = "Student is required";
    public static final String _THREAD_SUBJECT_REQUIRED = "Subject is required";
    public static final String _THREAD_SUBJECT_SIZE = "Subject must not exceed 255 characters";
    public static final String _THREAD_STATUS_REQUIRED = "Thread status is required";
    public static final String _THREAD_BODY_REQUIRED = "Message body is required";

    // Message
    public static final String _MESSAGE_SENDER_TYPE_REQUIRED = "Sender type is required";
    public static final String _MESSAGE_SENDER_REQUIRED = "Sender is required";
    public static final String _MESSAGE_BODY_REQUIRED = "Message body is required";
    public static final String _MESSAGE_ATTACHMENT_S3_KEY_SIZE = "Attachment S3 key must not exceed 500 characters";
    public static final String _MESSAGE_ATTACHMENT_NAME_SIZE = "Attachment name must not exceed 255 characters";
    public static final String _MESSAGE_READER_TYPE_REQUIRED = "Reader type is required";
    public static final String _MESSAGE_READER_REQUIRED = "Reader is required";

    // Notification
    public static final String _NOTIFICATION_RECIPIENT_TYPE_REQUIRED = "Recipient type is required";
    public static final String _NOTIFICATION_RECIPIENT_ID_REQUIRED = "Recipient ID is required";
    public static final String _NOTIFICATION_TYPE_REQUIRED = "Notification type is required";
    public static final String _NOTIFICATION_TITLE_REQUIRED = "Notification title is required";
    public static final String _NOTIFICATION_TITLE_SIZE = "Notification title must not exceed 255 characters";

    // Quiz
    public static final String _QUIZ_TITLE_REQUIRED = "Quiz title is required";
    public static final String _QUIZ_TITLE_SIZE = "Quiz title must not exceed 255 characters";
    public static final String _QUIZ_DESCRIPTION_SIZE = "Quiz description must not exceed 5000 characters";
    public static final String _QUIZ_CREATED_BY_REQUIRED = "Creator is required";
    public static final String _QUIZ_TOTAL_MARKS_REQUIRED = "Total marks is required";
    public static final String _QUIZ_TOTAL_MARKS_MIN = "Total marks must be greater than 0";
    public static final String _QUIZ_PASS_MARK_RANGE = "Pass mark percentage must be between 0 and 100";
    public static final String _QUIZ_WEIGHT_RANGE = "Weight percentage must be between 0 and 100";
    public static final String _QUIZ_MAX_ATTEMPTS_MIN = "Maximum attempts must be at least 1";
    public static final String _QUIZ_TIME_LIMIT_MIN = "Time limit must be at least 1 minute";
    public static final String _QUIZ_STATUS_REQUIRED = "Quiz status is required";
    public static final String _QUIZ_DOCUMENT_S3_KEY_SIZE = "Document S3 key must not exceed 500 characters";
    public static final String _QUIZ_DOCUMENT_NAME_SIZE = "Document name must not exceed 255 characters";

    // Quiz Question
    public static final String _QUESTION_TEXT_REQUIRED = "Question text is required";
    public static final String _QUESTION_TYPE_REQUIRED = "Question type is required";
    public static final String _QUESTION_MARKS_REQUIRED = "Question marks is required";
    public static final String _QUESTION_MARKS_MIN = "Question marks must be greater than 0";
    public static final String _QUESTION_SORT_ORDER_REQUIRED = "Sort order is required";
    public static final String _QUESTION_SORT_ORDER_MIN = "Sort order must be 0 or greater";
    public static final String _QUESTION_IMAGE_S3_KEY_SIZE = "Image S3 key must not exceed 500 characters";

    // Quiz Question Option
    public static final String _OPTION_TEXT_REQUIRED = "Option text is required";
    public static final String _OPTION_TEXT_SIZE = "Option text must not exceed 1000 characters";
    public static final String _OPTION_SORT_ORDER_REQUIRED = "Sort order is required";
    public static final String _OPTION_SORT_ORDER_MIN = "Sort order must be 0 or greater";

    // Quiz Attempt
    public static final String _ATTEMPT_STUDENT_REQUIRED = "Student is required";
    public static final String _ATTEMPT_QUESTION_REQUIRED = "Question is required";
    public static final String _ATTEMPT_ANSWER_TEXT_SIZE = "Answer text must not exceed 2000 characters";
}
