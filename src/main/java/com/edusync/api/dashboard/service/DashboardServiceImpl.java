package com.edusync.api.dashboard.service;

import com.edusync.api.actor.common.enums.UserRole;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.admission.enums.ApplicationStatus;
import com.edusync.api.admission.repo.StudentApplicationRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import com.edusync.api.course.assessment.submission.repo.SubmissionRepository;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.repo.AttendanceRepository;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import com.edusync.api.course.session.repo.ClassSessionRepository;
import com.edusync.api.dashboard.dto.*;
import com.edusync.api.dashboard.util.DashboardUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.edusync.api.dashboard.util.DashboardUtil.*;

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

        var lecturerCourses = courseRepository.findByLecturerId(lecturer.getId());
        var currentWeek = resolveCurrentWeekBounds();

        var weeklySessionCount = sessionRepository
                .findByLecturerIdAndScheduledStartBetween(lecturer.getId(), currentWeek.start(), currentWeek.end())
                .size();

        var totalEnrolledStudents = lecturerCourses.stream()
                .mapToLong(this::countEnrolledStudents)
                .sum();

        var courseSummaries = lecturerCourses.stream()
                .map(this::buildLecturerCourseSummary)
                .toList();

        return new LecturerDashboard(
                lecturerUuid,
                lecturerCourses.size(),
                totalEnrolledStudents,
                weeklySessionCount,
                courseSummaries
        );
    }

    @Override
    public StudentDashboard getStudentDashboard(UUID studentUuid) {
        var student = findStudentByUuid(studentUuid);

        var activeEnrollments = enrollmentRepository
                .findByStudentIdAndStatus(student.getId(), EnrollmentStatus.ENROLLED);

        var enrolledCourseIds = activeEnrollments.stream()
                .map(enrollment -> enrollment.getCourse().getId())
                .toList();

        var currentWeek = resolveCurrentWeekBounds();

        var weeklySessionCount = enrolledCourseIds.isEmpty() ? 0 :
                sessionRepository.findByCourseIdInAndScheduledStartBetween(enrolledCourseIds, currentWeek.start(), currentWeek.end()).size();

        var courseSummaries = activeEnrollments.stream()
                .map(enrollment -> buildStudentCourseSummary(student, enrollment))
                .toList();

        return new StudentDashboard(
                studentUuid,
                student.getStudentNumber(),
                activeEnrollments.size(),
                weeklySessionCount,
                courseSummaries
        );
    }

    @Override
    public List<CourseAttendanceOverview> getStudentAttendance(UUID studentUuid) {
        var student = findStudentByUuid(studentUuid);

        return enrollmentRepository.findByStudentId(student.getId()).stream()
                .map(enrollment -> buildCourseAttendanceOverview(student, enrollment.getCourse()))
                .toList();
    }

    @Override
    public SessionAttendanceOverview getSessionAttendance(UUID sessionUuid) {
        var session = sessionRepository.findByUuid(sessionUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Class session not found"));

        var attendanceRecords = attendanceRepository.findByClassSessionId(session.getId());
        var course = session.getModule().getCourse();

        var presentCount = countByAttendanceStatus(attendanceRecords, AttendanceStatus.PRESENT);
        var partialCount = countByAttendanceStatus(attendanceRecords, AttendanceStatus.PARTIAL);
        var absentCount = countByAttendanceStatus(attendanceRecords, AttendanceStatus.ABSENT);
        var attendancePct = calculatePercentage(presentCount + partialCount, attendanceRecords.size());

        var studentAttendanceDetails = attendanceRecords.stream()
                .map(DashboardUtil::mapToLecturerSessionAttendance)
                .toList();

        return new SessionAttendanceOverview(
                session.getUuid(), session.getTitle(),
                course.getCode(), course.getTitle(),
                session.getScheduledStart(), session.getScheduledEnd(),
                attendanceRecords.size(), presentCount, partialCount, absentCount,
                attendancePct, studentAttendanceDetails
        );
    }

    private LecturerCourseSummary buildLecturerCourseSummary(Course course) {
        var enrolledCount = countEnrolledStudents(course);
        var pendingSubmissions = submissionRepository.countByCourseIdAndStatus(course.getId(), SubmissionStatus.SUBMITTED);

        return new LecturerCourseSummary(
                course.getUuid(), course.getCode(), course.getTitle(),
                enrolledCount, pendingSubmissions
        );
    }

    private StudentCourseSummary buildStudentCourseSummary(Student student, CourseEnrollment enrollment) {
        var course = enrollment.getCourse();
        var courseId = course.getId();

        var attendancePct = calculateStudentCourseAttendancePct(student.getId(), courseId);
        var averageGradePct = calculateStudentCourseAverageGrade(student.getId(), courseId);
        var pendingAssessmentCount = submissionRepository.countPendingByStudentIdAndCourseId(student.getId(), courseId);

        return new StudentCourseSummary(
                course.getUuid(), course.getCode(), course.getTitle(),
                attendancePct, averageGradePct, pendingAssessmentCount
        );
    }

    private CourseAttendanceOverview buildCourseAttendanceOverview(Student student, Course course) {
        var attendanceRecords = attendanceRepository.findByStudentIdAndCourseId(student.getId(), course.getId());

        var presentCount = countByAttendanceStatus(attendanceRecords, AttendanceStatus.PRESENT);
        var partialCount = countByAttendanceStatus(attendanceRecords, AttendanceStatus.PARTIAL);
        var absentCount = countByAttendanceStatus(attendanceRecords, AttendanceStatus.ABSENT);
        var attendancePct = calculatePercentage(presentCount + partialCount, attendanceRecords.size());

        var totalDuration = sumDurationMinutes(attendanceRecords);

        var sessionDetails = attendanceRecords.stream()
                .map(AttendanceSessionDetail::from)
                .toList();

        return new CourseAttendanceOverview(
                course.getUuid(), course.getCode(), course.getTitle(),
                attendanceRecords.size(), presentCount, partialCount, absentCount,
                attendancePct, totalDuration > 0 ? totalDuration : null, sessionDetails
        );
    }

    private BigDecimal calculateStudentCourseAttendancePct(Long studentId, Long courseId) {
        var totalRecords = attendanceRepository.countByStudentIdAndCourseId(studentId, courseId);

        if (totalRecords == 0) return null;

        var presentCount = attendanceRepository.countByStudentIdAndCourseIdAndAttendanceStatus(studentId, courseId, AttendanceStatus.PRESENT);
        var partialCount = attendanceRepository.countByStudentIdAndCourseIdAndAttendanceStatus(studentId, courseId, AttendanceStatus.PARTIAL);

        return calculatePercentage(presentCount + partialCount, totalRecords);
    }

    private BigDecimal calculateStudentCourseAverageGrade(Long studentId, Long courseId) {
        var gradedSubmissions = submissionRepository.findByStudentId(studentId).stream()
                .filter(submission -> submission.getAssessment().getModule().getCourse().getId().equals(courseId))
                .filter(submission -> Objects.nonNull(submission.getMarksObtained()))
                .toList();

        if (gradedSubmissions.isEmpty()) return null;

        var totalGradePct = gradedSubmissions.stream()
                .map(DashboardUtil::calculateSubmissionPercentage)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalGradePct.divide(BigDecimal.valueOf(gradedSubmissions.size()), 2, RoundingMode.HALF_UP);
    }
    
    private Student findStudentByUuid(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    private long countEnrolledStudents(Course course) {
        return enrollmentRepository.findByCourseIdAndStatus(course.getId(), EnrollmentStatus.ENROLLED).size();
    }
}
