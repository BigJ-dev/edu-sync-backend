package com.edusync.api.course.assessment.grade.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface RubricGradeRequest {

    @Schema(name = "RubricGradeAward", description = "Grade a single rubric criterion for a submission. The DB trigger auto-computes total marks and auto-sets status to GRADED when all criteria are graded.")
    record Award(
            @NotNull(message = _RUBRIC_GRADE_GRADED_BY_REQUIRED)
            UUID gradedByUuid,

            @NotNull(message = _RUBRIC_GRADE_MARKS_REQUIRED)
            @DecimalMin(value = "0", message = _RUBRIC_GRADE_MARKS_MIN)
            BigDecimal marksAwarded,

            @Size(max = 5000, message = _RUBRIC_GRADE_COMMENT_SIZE)
            String comment
    ) implements RubricGradeRequest {}
}
