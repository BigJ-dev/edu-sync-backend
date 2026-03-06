package com.edusync.api.course.common.dto;

import com.edusync.api.course.common.enums.CourseStatus;
import com.edusync.api.course.common.model.Course;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CourseResponse(
        UUID uuid,
        String code,
        String title,
        String description,
        String thumbnailS3Key,
        UUID lecturerUuid,
        LocalDate startDate,
        LocalDate endDate,
        int minAttendancePct,
        Integer maxStudents,
        CourseStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getUuid(),
                course.getCode(),
                course.getTitle(),
                course.getDescription(),
                course.getThumbnailS3Key(),
                course.getLecturer().getUuid(),
                course.getStartDate(),
                course.getEndDate(),
                course.getMinAttendancePct(),
                course.getMaxStudents(),
                course.getStatus(),
                course.getCreatedAt(),
                course.getUpdatedAt()
        );
    }
}
