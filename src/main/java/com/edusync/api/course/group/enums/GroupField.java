package com.edusync.api.course.group.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupField {

    COURSE_ID("courseId"),
    MODULE("module"),
    NAME("name"),
    CREATED_BY("createdBy");

    private final String name;
}
