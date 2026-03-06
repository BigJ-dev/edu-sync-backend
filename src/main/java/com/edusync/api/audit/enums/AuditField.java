package com.edusync.api.audit.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuditField {

    ACTION("action"),
    ENTITY_TYPE("entityType"),
    ENTITY_ID("entityId"),
    PERFORMED_BY_TYPE("performedByType"),
    COURSE_ID("courseId"),
    CREATED_AT("createdAt");

    private final String name;
}
