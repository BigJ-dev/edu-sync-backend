package com.edusync.api.course.quiz.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QuizField {

    MODULE_ID("moduleId"),
    CREATED_BY("createdBy"),
    STATUS("status"),
    TITLE("title"),
    VISIBLE_FROM("visibleFrom");

    private final String name;
}
