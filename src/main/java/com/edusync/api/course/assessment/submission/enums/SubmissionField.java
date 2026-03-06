package com.edusync.api.course.assessment.submission.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubmissionField {

    ASSESSMENT_ID("assessmentId"),
    STUDENT_ID("studentId"),
    STATUS("status"),
    IS_LATE("isLate");

    private final String name;
}
