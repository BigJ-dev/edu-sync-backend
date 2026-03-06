package com.edusync.api.course.enrollment.service;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.service.CourseService;
import com.edusync.api.course.enrollment.dto.EnrollmentRequest;
import com.edusync.api.course.enrollment.dto.EnrollmentResponse;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository repository;
    private final CourseService courseService;
    private final StudentRepository studentRepository;

    public EnrollmentResponse enroll(UUID courseUuid, EnrollmentRequest.Enroll request) {
        var course = courseService.findCourse(courseUuid);
        var student = findStudent(request.studentUuid());

        if (repository.existsByCourseIdAndStudentId(course.getId(), student.getId()))
            throw new ServiceException(HttpStatus.CONFLICT, "Student is already enrolled in this course");

        var enrollment = CourseEnrollment.builder()
                .course(course)
                .student(student)
                .status(EnrollmentStatus.ENROLLED)
                .enrolledAt(Instant.now())
                .blocked(false)
                .build();

        return EnrollmentResponse.from(repository.save(enrollment));
    }

    @Transactional(readOnly = true)
    public Page<EnrollmentResponse> findAllByCourse(UUID courseUuid, EnrollmentStatus status, Boolean blocked, Pageable pageable) {
        var course = courseService.findCourse(courseUuid);
        var spec = Specification.where(EnrollmentSpec.hasCourseId(course.getId()))
                .and(EnrollmentSpec.hasStatus(status))
                .and(EnrollmentSpec.isBlocked(blocked));
        return repository.findAll(spec, pageable).map(EnrollmentResponse::from);
    }

    public EnrollmentResponse withdraw(UUID courseUuid, UUID studentUuid, EnrollmentRequest.Withdraw request) {
        var enrollment = findEnrollment(courseUuid, studentUuid);
        enrollment.setStatus(EnrollmentStatus.WITHDRAWN);
        enrollment.setWithdrawnAt(Instant.now());
        enrollment.setWithdrawalReason(request.withdrawalReason());
        return EnrollmentResponse.from(repository.save(enrollment));
    }

    public EnrollmentResponse block(UUID courseUuid, UUID studentUuid, EnrollmentRequest.Block request, Long blockedById) {
        var enrollment = findEnrollment(courseUuid, studentUuid);
        enrollment.setBlocked(true);
        enrollment.setBlockedAt(Instant.now());
        enrollment.setBlockedReason(request.blockedReason());
        return EnrollmentResponse.from(repository.save(enrollment));
    }

    public EnrollmentResponse unblock(UUID courseUuid, UUID studentUuid) {
        var enrollment = findEnrollment(courseUuid, studentUuid);
        enrollment.setBlocked(false);
        enrollment.setBlockedAt(null);
        enrollment.setBlockedBy(null);
        enrollment.setBlockedReason(null);
        return EnrollmentResponse.from(repository.save(enrollment));
    }

    private CourseEnrollment findEnrollment(UUID courseUuid, UUID studentUuid) {
        var course = courseService.findCourse(courseUuid);
        var student = findStudent(studentUuid);
        return repository.findByCourseIdAndStudentId(course.getId(), student.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Enrollment was not found"));
    }

    private Student findStudent(UUID studentUuid) {
        return studentRepository.findByUuid(studentUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }
}
