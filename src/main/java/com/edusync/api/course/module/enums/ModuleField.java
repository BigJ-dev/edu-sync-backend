package com.edusync.api.course.module.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModuleField {

    COURSE_ID("courseId"),
    TITLE("title"),
    STATUS("status"),
    SORT_ORDER("sortOrder");

    private final String name;
}
