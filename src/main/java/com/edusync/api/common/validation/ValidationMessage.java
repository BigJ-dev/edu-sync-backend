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
    PROFILE_IMAGE_KEY_SIZE("Profile image key must not exceed 500 characters");

    private final String message;

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
}
