package com.edusync.api.course.grading.dto;

import com.edusync.api.course.grading.enums.GradeCategoryType;
import com.edusync.api.course.grading.model.GradeWeightCategory;

import java.math.BigDecimal;
import java.util.UUID;

public record GradeWeightResponse(
        UUID uuid,
        UUID courseUuid,
        String name,
        GradeCategoryType categoryType,
        BigDecimal weightPct,
        int sortOrder
) {
    public static GradeWeightResponse from(GradeWeightCategory category) {
        return new GradeWeightResponse(
                category.getUuid(),
                category.getCourse().getUuid(),
                category.getName(),
                category.getCategoryType(),
                category.getWeightPct(),
                category.getSortOrder()
        );
    }
}
