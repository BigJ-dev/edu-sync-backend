package com.edusync.api.course.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CourseField {

    CODE("code"),
    TITLE("title"),
    STATUS("status"),
    LECTURER_ID("lecturerId"),
    START_DATE("startDate"),
    END_DATE("endDate");

    private final String name;
}
