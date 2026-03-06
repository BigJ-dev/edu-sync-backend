package com.edusync.api.course.enrollment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnrollmentField {

    COURSE_ID("courseId"),
    STUDENT_ID("studentId"),
    STATUS("status"),
    BLOCKED("blocked");

    private final String name;
}
