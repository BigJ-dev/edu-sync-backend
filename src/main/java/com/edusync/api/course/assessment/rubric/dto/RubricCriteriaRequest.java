package com.edusync.api.course.assessment.rubric.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface RubricCriteriaRequest {

    @Schema(name = "RubricCriteriaCreate")
    record Create(
            @NotBlank(message = _RUBRIC_TITLE_REQUIRED)
            @Size(max = 255, message = _RUBRIC_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _RUBRIC_DESCRIPTION_SIZE)
            String description,

            @NotNull(message = _RUBRIC_MAX_MARKS_REQUIRED)
            @DecimalMin(value = "0.01", message = _RUBRIC_MAX_MARKS_MIN)
            BigDecimal maxMarks,

            @NotNull(message = _RUBRIC_SORT_ORDER_REQUIRED)
            @Min(value = 0, message = _RUBRIC_SORT_ORDER_MIN)
            Integer sortOrder
    ) implements RubricCriteriaRequest {}

    @Schema(name = "RubricCriteriaUpdate")
    record Update(
            @NotBlank(message = _RUBRIC_TITLE_REQUIRED)
            @Size(max = 255, message = _RUBRIC_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _RUBRIC_DESCRIPTION_SIZE)
            String description,

            @NotNull(message = _RUBRIC_MAX_MARKS_REQUIRED)
            @DecimalMin(value = "0.01", message = _RUBRIC_MAX_MARKS_MIN)
            BigDecimal maxMarks,

            @NotNull(message = _RUBRIC_SORT_ORDER_REQUIRED)
            @Min(value = 0, message = _RUBRIC_SORT_ORDER_MIN)
            Integer sortOrder
    ) implements RubricCriteriaRequest {}
}
