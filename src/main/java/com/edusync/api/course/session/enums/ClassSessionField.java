package com.edusync.api.course.session.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClassSessionField {

    MODULE_ID("moduleId"),
    LECTURER_ID("lecturerId"),
    SESSION_TYPE("sessionType"),
    STATUS("status"),
    TITLE("title"),
    SCHEDULED_START("scheduledStart");

    private final String name;
}
