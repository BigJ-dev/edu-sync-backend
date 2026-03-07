package com.edusync.api.course.grading.dto;

import com.edusync.api.course.grading.enums.GradeCategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public sealed interface GradeWeightRequest {

    @Schema(name = "GradeWeightCreate")
    record Create(
            @NotBlank(message = "Category name is required")
            @Size(max = 100, message = "Category name must not exceed 100 characters")
            String name,

            @NotNull(message = "Category type is required")
            GradeCategoryType categoryType,

            @NotNull(message = "Weight percentage is required")
            @DecimalMin(value = "0.00", message = "Weight must be at least 0")
            @DecimalMax(value = "100.00", message = "Weight must not exceed 100")
            BigDecimal weightPct,

            int sortOrder
    ) implements GradeWeightRequest {}

    @Schema(name = "GradeWeightUpdate")
    record Update(
            @Size(max = 100, message = "Category name must not exceed 100 characters")
            String name,

            @DecimalMin(value = "0.00", message = "Weight must be at least 0")
            @DecimalMax(value = "100.00", message = "Weight must not exceed 100")
            BigDecimal weightPct,

            Integer sortOrder
    ) implements GradeWeightRequest {}
}
