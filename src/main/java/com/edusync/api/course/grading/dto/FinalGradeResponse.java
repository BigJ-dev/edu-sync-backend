package com.edusync.api.course.grading.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record FinalGradeResponse(
        UUID courseUuid,
        UUID studentUuid,
        BigDecimal finalGradePct,
        BigDecimal totalWeightConfigured,
        List<CategoryGradeDetail> categories
) {}
