package com.edusync.api.dashboard.util;

import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.model.AttendanceRecord;
import com.edusync.api.dashboard.dto.LecturerSessionAttendance;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DashboardUtil {

    private static final int PERCENTAGE_SCALE = 2;

    public static BigDecimal calculatePercentage(long numerator, long denominator) {
        if (denominator == 0) return null;

        return BigDecimal.valueOf(numerator)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(denominator), PERCENTAGE_SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateSubmissionPercentage(AssessmentSubmission submission) {
        return submission.getMarksObtained()
                .multiply(BigDecimal.valueOf(100))
                .divide(submission.getAssessment().getTotalMarks(), PERCENTAGE_SCALE, RoundingMode.HALF_UP);
    }

    public static long countByAttendanceStatus(List<AttendanceRecord> records, AttendanceStatus status) {
        return records.stream()
                .filter(record -> record.getAttendanceStatus() == status)
                .count();
    }

    public static int sumDurationMinutes(List<AttendanceRecord> records) {
        return records.stream()
                .map(AttendanceRecord::getTotalDurationMinutes)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
    }

    public static LecturerSessionAttendance mapToLecturerSessionAttendance(AttendanceRecord record) {
        var student = record.getStudent();

        return new LecturerSessionAttendance(
                student.getUuid(),
                student.getStudentNumber(),
                student.getFirstName(),
                student.getLastName(),
                record.getAttendanceStatus(),
                record.getJoinTime(),
                record.getLeaveTime(),
                record.getTotalDurationMinutes(),
                record.isSyncedFromTeams()
        );
    }

    public static WeekBounds resolveCurrentWeekBounds() {
        var today = LocalDate.now();
        var weekStart = today.with(DayOfWeek.MONDAY).atStartOfDay().toInstant(ZoneOffset.UTC);
        var weekEnd = today.with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        return new WeekBounds(weekStart, weekEnd);
    }

    public record WeekBounds(Instant start, Instant end) {}
}
