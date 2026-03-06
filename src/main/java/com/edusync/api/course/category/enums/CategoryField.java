package com.edusync.api.course.category.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryField {

    NAME("name"),
    PARENT("parent"),
    ACTIVE("active"),
    SORT_ORDER("sortOrder");

    private final String name;
}
