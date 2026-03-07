package com.edusync.api.dashboard.service;

import com.edusync.api.actor.common.enums.UserRole;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.admission.enums.ApplicationStatus;
import com.edusync.api.admission.repo.StudentApplicationRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import com.edusync.api.course.assessment.submission.repo.SubmissionRepository;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.repo.AttendanceRepository;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import com.edusync.api.course.session.repo.ClassSessionRepository;
import com.edusync.api.dashboard.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentApplicationRepository applicationRepository;
    private final ClassSessionRepository sessionRepository;
    private final SubmissionRepository submissionRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public AdminDashboard getAdminDashboard() {
        return new AdminDashboard(
                studentRepository.count(),
                studentRepository.countByActive(true),
                courseRepository.count(),
                appUserRepository.countByRole(UserRole.LECTURER),
                applicationRepository.countByStatus(ApplicationStatus.PENDING),
                applicationRepository.countByStatus(ApplicationStatus.APPROVED),
                applicationRepository.countByStatus(ApplicationStatus.REJECTED),
                enrollmentRepository.count()
        );
    }

    @Override
    public LecturerDashboard getLecturerDashboard(UUID lecturerUuid) {
        var lecturer = appUserRepository.findByUuidAndRole(lecturerUuid, UserRole.LECTURER)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Lecturer not found"));

        var courses = courseRepository.findByLecturerId(lecturer.getId());

        var weekStart = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay().toInstant(ZoneOffset.UTC);
        var weekEnd = LocalDate.now().with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        var upcomingSessions = sessionRepository.findByLecturerIdAndScheduledStartBetween(lecturer.getId(), weekStart, weekEnd).size();

        var totalStudents = courses.stream()
                .mapToLong(c -> enrollmentRepository.findByCourseIdAndStatus(c.getId(), EnrollmentStatus.ENROLLED).size())
                .sum();

        var courseSummaries = courses.stream()
                .map(course -> {
                    var enrolled = enrollmentRepository.findByCourseIdAndStatus(course.getId(), EnrollmentStatus.ENROLLED).size();
                    var pending = submissionRepository.countByCourseIdAndStatus(course.getId(), SubmissionStatus.SUBMITTED);
                    return new LecturerCourseSummary(
                            course.getUuid(), course.getCode(), course.getTitle(),
                            enrolled, pending
                    );
                })
                .toList();

        return new LecturerDashboard(
                lecturerUuid,
                courses.size(),
                totalStudents,
                upcomingSessions,
                courseSummaries
        );
    }

    @Override
    public StudentDashboard getStudentDashboard(UUID studentUuid) {
        var student = studentRepository.findByUuid(studentUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student not found"));

        var enrollments = enrollmentRepository.findByStudentIdAndStatus(student.getId(), EnrollmentStatus.ENROLLED);

        var courseIds = enrollments.stream().map(e -> e.getCourse().getId()).toList();

        var weekStart = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay().toInstant(ZoneOffset.UTC);
        var weekEnd = LocalDate.now().with(DayOfWeek.SUNDAY).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        var upcomingSessions = courseIds.isEmpty() ? 0 :
                sessionRepository.findByCourseIdInAndScheduledStartBetween(courseIds, weekStart, weekEnd).size();

        var courseSummaries = enrollments.stream()
                .map(enrollment -> {
                    var course = enrollment.getCourse();
                    var courseId = course.getId();

                    var totalAttendance = attendanceRepository.countByStudentIdAndCourseId(student.getId(), courseId);
                    BigDecimal attendancePct = null;
                    if (totalAttendance > 0) {
                        var present = attendanceRepository.countByStudentIdAndCourseIdAndAttendanceStatus(student.getId(), courseId, AttendanceStatus.PRESENT);
                        var partial = attendanceRepository.countByStudentIdAndCourseIdAndAttendanceStatus(student.getId(), courseId, AttendanceStatus.PARTIAL);
                        attendancePct = BigDecimal.valueOf(present + partial)
                                .multiply(BigDecimal.valueOf(100))
                                .divide(BigDecimal.valueOf(totalAttendance), 2, RoundingMode.HALF_UP);
                    }

                    var submissions = submissionRepository.findByStudentId(student.getId()).stream()
                            .filter(s -> s.getAssessment().getModule().getCourse().getId().equals(courseId))
                            .filter(s -> Objects.nonNull(s.getMarksObtained()))
                            .toList();

                    BigDecimal avgGrade = submissions.isEmpty() ? null :
                            submissions.stream()
                                    .map(s -> s.getMarksObtained()
                                            .multiply(BigDecimal.valueOf(100))
                                            .divide(s.getAssessment().getTotalMarks(), 2, RoundingMode.HALF_UP))
                                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                                    .divide(BigDecimal.valueOf(submissions.size()), 2, RoundingMode.HALF_UP);

                    var pendingAssessments = submissionRepository.countPendingByStudentIdAndCourseId(student.getId(), courseId);

                    return new StudentCourseSummary(
                            course.getUuid(), course.getCode(), course.getTitle(),
                            attendancePct, avgGrade, pendingAssessments
                    );
                })
                .toList();

        return new StudentDashboard(
                studentUuid,
                student.getStudentNumber(),
                enrollments.size(),
                upcomingSessions,
                courseSummaries
        );
    }

    @Override
    public List<CourseAttendanceOverview> getStudentAttendance(UUID studentUuid) {
        var student = studentRepository.findByUuid(studentUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student not found"));

        var enrollments = enrollmentRepository.findByStudentId(student.getId());

        return enrollments.stream()
                .map(enrollment -> {
                    var course = enrollment.getCourse();
                    var records = attendanceRepository.findByStudentIdAndCourseId(student.getId(), course.getId());

                    var presentCount = records.stream().filter(r -> r.getAttendanceStatus() == AttendanceStatus.PRESENT).count();
                    var partialCount = records.stream().filter(r -> r.getAttendanceStatus() == AttendanceStatus.PARTIAL).count();
                    var absentCount = records.stream().filter(r -> r.getAttendanceStatus() == AttendanceStatus.ABSENT).count();
                    var totalSessions = records.size();

                    var attendancePct = totalSessions == 0 ? null :
                            BigDecimal.valueOf(presentCount + partialCount)
                                    .multiply(BigDecimal.valueOf(100))
                                    .divide(BigDecimal.valueOf(totalSessions), 2, RoundingMode.HALF_UP);

                    var totalDuration = records.stream()
                            .map(r -> Objects.nonNull(r.getTotalDurationMinutes()) ? r.getTotalDurationMinutes() : 0)
                            .reduce(0, Integer::sum);

                    var sessions = records.stream()
                            .map(AttendanceSessionDetail::from)
                            .toList();

                    return new CourseAttendanceOverview(
                            course.getUuid(), course.getCode(), course.getTitle(),
                            totalSessions, presentCount, partialCount, absentCount,
                            attendancePct, totalDuration > 0 ? totalDuration : null, sessions
                    );
                })
                .toList();
    }

    @Override
    public SessionAttendanceOverview getSessionAttendance(UUID sessionUuid) {
        var session = sessionRepository.findByUuid(sessionUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Class session not found"));

        var records = attendanceRepository.findByClassSessionId(session.getId());

        var presentCount = records.stream().filter(r -> r.getAttendanceStatus() == AttendanceStatus.PRESENT).count();
        var partialCount = records.stream().filter(r -> r.getAttendanceStatus() == AttendanceStatus.PARTIAL).count();
        var absentCount = records.stream().filter(r -> r.getAttendanceStatus() == AttendanceStatus.ABSENT).count();
        var totalStudents = records.size();

        var attendancePct = totalStudents == 0 ? null :
                BigDecimal.valueOf(presentCount + partialCount)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(totalStudents), 2, RoundingMode.HALF_UP);

        var students = records.stream()
                .map(r -> new LecturerSessionAttendance(
                        r.getStudent().getUuid(),
                        r.getStudent().getStudentNumber(),
                        r.getStudent().getFirstName(),
                        r.getStudent().getLastName(),
                        r.getAttendanceStatus(),
                        r.getJoinTime(),
                        r.getLeaveTime(),
                        r.getTotalDurationMinutes(),
                        r.isSyncedFromTeams()
                ))
                .toList();

        var course = session.getModule().getCourse();

        return new SessionAttendanceOverview(
                session.getUuid(), session.getTitle(),
                course.getCode(), course.getTitle(),
                session.getScheduledStart(), session.getScheduledEnd(),
                totalStudents, presentCount, partialCount, absentCount,
                attendancePct, students
        );
    }
}
