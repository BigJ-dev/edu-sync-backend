package com.edusync.api.course.broadcast.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BroadcastField {

    COURSE_ID("courseId"),
    SENT_BY("sentBy"),
    TARGET_TYPE("targetType"),
    PRIORITY("priority"),
    TITLE("title"),
    SENT_AT("sentAt");

    private final String name;
}
