package com.edusync.api.course.material.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MaterialField {

    MODULE_ID("moduleId"),
    CLASS_SESSION_ID("classSessionId"),
    MATERIAL_TYPE("materialType"),
    TITLE("title"),
    VISIBLE_TO_STUDENTS("visibleToStudents"),
    SORT_ORDER("sortOrder");

    private final String name;
}
