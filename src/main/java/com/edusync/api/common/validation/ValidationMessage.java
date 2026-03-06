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
    ENROLLMENT_WITHDRAWAL_REASON_SIZE("Withdrawal reason must not exceed 500 characters");

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
}
