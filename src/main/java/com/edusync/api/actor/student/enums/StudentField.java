package com.edusync.api.actor.student.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudentField {

    ACTIVE("active"),
    FIRST_NAME("firstName"),
    LAST_NAME("lastName"),
    EMAIL("email"),
    STUDENT_NUMBER("studentNumber");

    private final String name;
}
