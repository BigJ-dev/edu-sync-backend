package com.edusync.api.course.assessment.common.dto;

import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.common.enums.DeliveryMode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface AssessmentRequest {

    @Schema(name = "AssessmentCreate")
    record Create(
            @NotBlank(message = _ASSESSMENT_TITLE_REQUIRED)
            @Size(max = 255, message = _ASSESSMENT_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _ASSESSMENT_DESCRIPTION_SIZE)
            String description,

            @NotNull(message = _ASSESSMENT_TYPE_REQUIRED)
            AssessmentType assessmentType,

            @NotNull(message = _ASSESSMENT_DELIVERY_MODE_REQUIRED)
            DeliveryMode deliveryMode,

            @NotNull(message = _ASSESSMENT_TOTAL_MARKS_REQUIRED)
            @DecimalMin(value = "0.01", message = _ASSESSMENT_TOTAL_MARKS_MIN)
            BigDecimal totalMarks,

            @DecimalMin(value = "0", message = _ASSESSMENT_WEIGHT_RANGE)
            @DecimalMax(value = "100", message = _ASSESSMENT_WEIGHT_RANGE)
            BigDecimal weightPct,

            @NotNull(message = _ASSESSMENT_DUE_DATE_REQUIRED)
            Instant dueDate,

            Instant visibleFrom,

            Boolean allowLateSubmission,

            @DecimalMin(value = "0", message = _ASSESSMENT_PENALTY_RANGE)
            @DecimalMax(value = "100", message = _ASSESSMENT_PENALTY_RANGE)
            BigDecimal latePenaltyPct,

            @Size(max = 500, message = _ASSESSMENT_BRIEF_S3_KEY_SIZE)
            String briefS3Key,

            @Size(max = 255, message = _ASSESSMENT_BRIEF_FILE_NAME_SIZE)
            String briefFileName,

            @NotNull(message = _ASSESSMENT_CREATED_BY_REQUIRED)
            UUID createdByUuid
    ) implements AssessmentRequest {}

    @Schema(name = "AssessmentUpdate")
    record Update(
            @NotBlank(message = _ASSESSMENT_TITLE_REQUIRED)
            @Size(max = 255, message = _ASSESSMENT_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _ASSESSMENT_DESCRIPTION_SIZE)
            String description,

            @NotNull(message = _ASSESSMENT_TYPE_REQUIRED)
            AssessmentType assessmentType,

            @NotNull(message = _ASSESSMENT_DELIVERY_MODE_REQUIRED)
            DeliveryMode deliveryMode,

            @NotNull(message = _ASSESSMENT_TOTAL_MARKS_REQUIRED)
            @DecimalMin(value = "0.01", message = _ASSESSMENT_TOTAL_MARKS_MIN)
            BigDecimal totalMarks,

            @DecimalMin(value = "0", message = _ASSESSMENT_WEIGHT_RANGE)
            @DecimalMax(value = "100", message = _ASSESSMENT_WEIGHT_RANGE)
            BigDecimal weightPct,

            @NotNull(message = _ASSESSMENT_DUE_DATE_REQUIRED)
            Instant dueDate,

            Instant visibleFrom,

            Boolean allowLateSubmission,

            @DecimalMin(value = "0", message = _ASSESSMENT_PENALTY_RANGE)
            @DecimalMax(value = "100", message = _ASSESSMENT_PENALTY_RANGE)
            BigDecimal latePenaltyPct,

            @Size(max = 500, message = _ASSESSMENT_BRIEF_S3_KEY_SIZE)
            String briefS3Key,

            @Size(max = 255, message = _ASSESSMENT_BRIEF_FILE_NAME_SIZE)
            String briefFileName
    ) implements AssessmentRequest {}

    @Schema(name = "AssessmentUpdateStatus")
    record UpdateStatus(
            @NotNull(message = _ASSESSMENT_STATUS_REQUIRED)
            AssessmentStatus status
    ) implements AssessmentRequest {}

    @Schema(name = "AssessmentReopen", description = "Reopen a closed/graded assessment with a new due date so students can submit again.")
    record Reopen(
            @NotNull(message = _ASSESSMENT_DUE_DATE_REQUIRED)
            Instant newDueDate,

            Instant visibleFrom,

            Boolean allowLateSubmission
    ) implements AssessmentRequest {}
}
