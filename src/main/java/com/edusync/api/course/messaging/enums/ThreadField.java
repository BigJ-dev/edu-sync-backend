package com.edusync.api.course.messaging.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ThreadField {

    COURSE_ID("courseId"),
    STUDENT("student"),
    STATUS("status"),
    PRIORITY("priority"),
    IS_ESCALATED("isEscalated"),
    LAST_MESSAGE_AT("lastMessageAt"),
    SUBJECT("subject");

    private final String name;
}
