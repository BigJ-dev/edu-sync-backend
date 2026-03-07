package com.edusync.api.admission.dto;

import com.edusync.api.admission.model.ApplicationCourseChoice;

import java.util.UUID;

public record ApplicationCourseChoiceResponse(
        int priority,
        UUID courseUuid,
        String courseCode,
        String courseTitle
) {
    public static ApplicationCourseChoiceResponse from(ApplicationCourseChoice choice) {
        return new ApplicationCourseChoiceResponse(
                choice.getPriority(),
                choice.getCourse().getUuid(),
                choice.getCourse().getCode(),
                choice.getCourse().getTitle()
        );
    }
}
