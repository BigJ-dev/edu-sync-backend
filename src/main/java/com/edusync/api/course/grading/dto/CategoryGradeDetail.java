package com.edusync.api.course.grading.dto;

import com.edusync.api.course.grading.enums.GradeCategoryType;

import java.math.BigDecimal;

public record CategoryGradeDetail(
        String categoryName,
        GradeCategoryType categoryType,
        BigDecimal weightPct,
        BigDecimal averagePct,
        BigDecimal weightedContribution,
        int itemsGraded,
        int totalItems
) {}
