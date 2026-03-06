package com.edusync.api.course.assessment.submission.dto;

import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface SubmissionRequest {

    @Schema(name = "SubmissionSubmit", description = "Submit an assessment. For ONLINE delivery: provide submissionText (from the online editor). For PHYSICAL delivery: provide s3Key, fileName (uploaded document).")
    record Submit(
            @NotNull(message = _SUBMISSION_STUDENT_REQUIRED)
            UUID studentUuid,

            String submissionText,

            @Size(max = 500, message = _SUBMISSION_S3_KEY_SIZE)
            String s3Key,

            @Size(max = 255, message = _SUBMISSION_FILE_NAME_SIZE)
            String fileName,

            Long fileSizeBytes,

            @Size(max = 100, message = _SUBMISSION_MIME_TYPE_SIZE)
            String mimeType
    ) implements SubmissionRequest {}

    @Schema(name = "SubmissionGrade", description = "Grade a submission directly (without rubric). Sets marks, feedback, and status to GRADED.")
    record Grade(
            @NotNull(message = _RUBRIC_GRADE_GRADED_BY_REQUIRED)
            UUID gradedByUuid,

            @NotNull(message = _RUBRIC_GRADE_MARKS_REQUIRED)
            @DecimalMin(value = "0", message = _SUBMISSION_MARKS_MIN)
            BigDecimal marksObtained,

            @Size(max = 5000, message = _SUBMISSION_FEEDBACK_SIZE)
            String feedback
    ) implements SubmissionRequest {}

    @Schema(name = "SubmissionReturn", description = "Return a submission for resubmission with feedback.")
    record Return(
            @Size(max = 5000, message = _SUBMISSION_FEEDBACK_SIZE)
            String feedback
    ) implements SubmissionRequest {}
}
