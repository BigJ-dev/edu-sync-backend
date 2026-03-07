package com.edusync.api.teams.util;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.model.AttendanceRecord;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.teams.dto.TeamsResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeamsReportBuilder {

    private static final int PERCENTAGE_SCALE = 2;
    private static final int NO_DURATION = 0;

    public record MonthlyDateRange(Instant from, Instant to) {}

    public static List<TeamsResponse.StudentAttendanceSummary> buildStudentAttendanceSummaries(List<CourseEnrollment> enrollments, Map<Long, List<AttendanceRecord>> recordsByStudent, int totalSessions) {
        return enrollments.stream().map(enrollment -> buildSingleStudentAttendanceSummary(enrollment.getStudent(), recordsByStudent, totalSessions)).toList();
    }

    public static TeamsResponse.StudentAttendanceSummary buildSingleStudentAttendanceSummary(Student student, Map<Long, List<AttendanceRecord>> recordsByStudent, int totalSessions) {
        var records = recordsByStudent.getOrDefault(student.getId(), Collections.emptyList());
        int attendedSessions = countAttendedSessions(records);
        int totalAttendanceMinutes = sumTotalAttendanceDurationMinutes(records);
        var attendancePercentage = calculateAttendancePercentage(attendedSessions, totalSessions);

        return new TeamsResponse.StudentAttendanceSummary(student.getUuid(), student.getStudentNumber(), student.getFirstName(), student.getLastName(), totalSessions, attendedSessions, attendancePercentage, totalAttendanceMinutes);
    }

    public static List<TeamsResponse.ClassAttendanceSummary> buildClassAttendanceSummaries(List<ClassSession> sessions, Map<Long, List<AttendanceRecord>> recordsBySession, int totalEnrolled) {
        return sessions.stream().map(session -> buildSingleClassAttendanceSummary(session, recordsBySession, totalEnrolled)).toList();
    }

    public static TeamsResponse.ClassAttendanceSummary buildSingleClassAttendanceSummary(ClassSession session, Map<Long, List<AttendanceRecord>> recordsBySession, int totalEnrolled) {
        var records = recordsBySession.getOrDefault(session.getId(), Collections.emptyList());
        int attendedCount = countAttendedSessions(records);
        int averageDurationMinutes = calculateAverageAttendanceDuration(records);
        var attendancePercentage = calculateAttendancePercentage(attendedCount, totalEnrolled);
        var lecturer = session.getLecturer();

        return new TeamsResponse.ClassAttendanceSummary(session.getUuid(), session.getTitle(), session.getScheduledStart(), session.getSessionType(), Objects.nonNull(session.getTeamsMeetingId()), lecturer.getUuid(), lecturer.getFirstName(), lecturer.getLastName(), totalEnrolled, attendedCount, attendancePercentage, averageDurationMinutes);
    }

    public static int countAttendedSessions(List<AttendanceRecord> records) {
        return (int) records.stream().filter(r -> r.getAttendanceStatus() == AttendanceStatus.PRESENT || r.getAttendanceStatus() == AttendanceStatus.PARTIAL).count();
    }

    public static int sumTotalAttendanceDurationMinutes(List<AttendanceRecord> records) {
        return records.stream().filter(r -> Objects.nonNull(r.getTotalDurationMinutes())).mapToInt(AttendanceRecord::getTotalDurationMinutes).sum();
    }

    public static int calculateAverageAttendanceDuration(List<AttendanceRecord> records) {
        return records.isEmpty() ? NO_DURATION : (int) records.stream().filter(r -> Objects.nonNull(r.getTotalDurationMinutes())).mapToInt(AttendanceRecord::getTotalDurationMinutes).average().orElse(NO_DURATION);
    }

    public static BigDecimal calculateAttendancePercentage(int numerator, int denominator) {
        if (denominator == 0) return BigDecimal.ZERO;
        return BigDecimal.valueOf(numerator).multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(denominator), PERCENTAGE_SCALE, RoundingMode.HALF_UP);
    }

    public static Map<Long, List<AttendanceRecord>> groupAttendanceRecordsByStudentId(List<AttendanceRecord> records) {
        return records.stream().collect(Collectors.groupingBy(r -> r.getStudent().getId()));
    }

    public static Map<Long, List<AttendanceRecord>> groupAttendanceRecordsBySessionId(List<AttendanceRecord> records) {
        return records.stream().collect(Collectors.groupingBy(r -> r.getClassSession().getId()));
    }

    public static MonthlyDateRange buildMonthlyDateRange(int year, int month) {
        var yearMonth = YearMonth.of(year, month);
        var from = yearMonth.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        var to = yearMonth.plusMonths(1).atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC);
        return new MonthlyDateRange(from, to);
    }
}
