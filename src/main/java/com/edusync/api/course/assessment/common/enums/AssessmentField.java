package com.edusync.api.course.assessment.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AssessmentField {

    MODULE_ID("moduleId"),
    ASSESSMENT_TYPE("assessmentType"),
    DELIVERY_MODE("deliveryMode"),
    STATUS("status"),
    TITLE("title"),
    DUE_DATE("dueDate");

    private final String name;
}
