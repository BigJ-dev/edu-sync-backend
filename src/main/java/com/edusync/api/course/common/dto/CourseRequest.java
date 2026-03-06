package com.edusync.api.course.common.dto;

import com.edusync.api.course.common.enums.CourseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.UUID;

import static com.edusync.api.common.validation.ValidationMessage.*;

public sealed interface CourseRequest {

    @Schema(name = "CourseCreate")
    record Create(
            @NotBlank(message = _COURSE_CODE_REQUIRED)
            @Size(max = 20, message = _COURSE_CODE_SIZE)
            String code,

            @NotBlank(message = _COURSE_TITLE_REQUIRED)
            @Size(max = 255, message = _COURSE_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _COURSE_DESCRIPTION_SIZE)
            String description,

            @Size(max = 500, message = _COURSE_THUMBNAIL_SIZE)
            String thumbnailS3Key,

            @NotNull(message = _COURSE_LECTURER_REQUIRED)
            UUID lecturerUuid,

            @NotNull(message = _COURSE_START_DATE_REQUIRED)
            LocalDate startDate,

            @NotNull(message = _COURSE_END_DATE_REQUIRED)
            LocalDate endDate,

            @Min(value = 0, message = _COURSE_MIN_ATTENDANCE_RANGE)
            @Max(value = 100, message = _COURSE_MIN_ATTENDANCE_RANGE)
            Integer minAttendancePct,

            Integer maxStudents
    ) implements CourseRequest {}

    @Schema(name = "CourseUpdate")
    record Update(
            @NotBlank(message = _COURSE_TITLE_REQUIRED)
            @Size(max = 255, message = _COURSE_TITLE_SIZE)
            String title,

            @Size(max = 5000, message = _COURSE_DESCRIPTION_SIZE)
            String description,

            @Size(max = 500, message = _COURSE_THUMBNAIL_SIZE)
            String thumbnailS3Key,

            @NotNull(message = _COURSE_START_DATE_REQUIRED)
            LocalDate startDate,

            @NotNull(message = _COURSE_END_DATE_REQUIRED)
            LocalDate endDate,

            @Min(value = 0, message = _COURSE_MIN_ATTENDANCE_RANGE)
            @Max(value = 100, message = _COURSE_MIN_ATTENDANCE_RANGE)
            Integer minAttendancePct,

            Integer maxStudents
    ) implements CourseRequest {}

    @Schema(name = "CourseUpdateStatus")
    record UpdateStatus(
            @NotNull(message = _COURSE_STATUS_REQUIRED)
            CourseStatus status
    ) implements CourseRequest {}
}
