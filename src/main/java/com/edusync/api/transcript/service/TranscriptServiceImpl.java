package com.edusync.api.transcript.service;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.repo.AttendanceRepository;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import com.edusync.api.course.assessment.submission.repo.SubmissionRepository;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import com.edusync.api.transcript.dto.TranscriptAssessmentEntry;
import com.edusync.api.transcript.dto.TranscriptCourseEntry;
import com.edusync.api.transcript.dto.TranscriptResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TranscriptServiceImpl implements TranscriptService {

    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SubmissionRepository submissionRepository;
    private final AttendanceRepository attendanceRepository;

    @Override
    public TranscriptResponse getStudentTranscript(UUID studentUuid) {
        var student = studentRepository.findByUuid(studentUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student not found"));

        var enrollments = enrollmentRepository.findByStudentId(student.getId());
        var allSubmissions = submissionRepository.findByStudentId(student.getId());

        var courses = enrollments.stream()
                .map(enrollment -> buildCourseEntry(student, enrollment, allSubmissions))
                .toList();

        return new TranscriptResponse(
                student.getUuid(),
                student.getStudentNumber(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                courses
        );
    }

    private TranscriptCourseEntry buildCourseEntry(Student student, CourseEnrollment enrollment, List<AssessmentSubmission> allSubmissions) {
        var course = enrollment.getCourse();
        var courseId = course.getId();

        var courseSubmissions = allSubmissions.stream()
                .filter(s -> s.getAssessment().getModule().getCourse().getId().equals(courseId))
                .toList();

        var assessments = courseSubmissions.stream()
                .map(this::toAssessmentEntry)
                .toList();

        var attendancePct = calculateAttendancePct(student.getId(), courseId);

        var gradedSubmissions = courseSubmissions.stream()
                .filter(s -> Objects.nonNull(s.getMarksObtained()))
                .toList();

        var averageGrade = gradedSubmissions.isEmpty() ? null :
                gradedSubmissions.stream()
                        .map(s -> s.getMarksObtained()
                                .multiply(BigDecimal.valueOf(100))
                                .divide(s.getAssessment().getTotalMarks(), 2, RoundingMode.HALF_UP))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(gradedSubmissions.size()), 2, RoundingMode.HALF_UP);

        return new TranscriptCourseEntry(
                course.getUuid(),
                course.getCode(),
                course.getTitle(),
                enrollment.getStatus(),
                enrollment.getEnrolledAt(),
                attendancePct,
                averageGrade,
                enrollment.getFinalGrade(),
                assessments
        );
    }

    private TranscriptAssessmentEntry toAssessmentEntry(AssessmentSubmission submission) {
        var assessment = submission.getAssessment();
        return new TranscriptAssessmentEntry(
                assessment.getUuid(),
                assessment.getTitle(),
                assessment.getAssessmentType(),
                assessment.getModule().getTitle(),
                assessment.getTotalMarks(),
                submission.getMarksObtained(),
                assessment.getWeightPct(),
                submission.getSubmittedAt(),
                submission.isLate()
        );
    }

    private BigDecimal calculateAttendancePct(Long studentId, Long courseId) {
        var totalSessions = attendanceRepository.countByStudentIdAndCourseId(studentId, courseId);
        if (totalSessions == 0) return null;

        var presentCount = attendanceRepository.countByStudentIdAndCourseIdAndAttendanceStatus(studentId, courseId, AttendanceStatus.PRESENT);
        var partialCount = attendanceRepository.countByStudentIdAndCourseIdAndAttendanceStatus(studentId, courseId, AttendanceStatus.PARTIAL);

        var attended = presentCount + partialCount;
        return BigDecimal.valueOf(attended)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalSessions), 2, RoundingMode.HALF_UP);
    }
}
