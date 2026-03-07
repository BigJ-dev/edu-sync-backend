package com.edusync.api.course.enrollment.service;

import com.edusync.api.course.enrollment.dto.EnrollmentRequest;
import com.edusync.api.course.enrollment.dto.EnrollmentResponse;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EnrollmentService {

    EnrollmentResponse enrollStudentInCourse(UUID courseUuid, EnrollmentRequest.Enroll request);

    Page<EnrollmentResponse> findAllEnrollmentsByCourse(UUID courseUuid, EnrollmentStatus status, Boolean blocked, Pageable pageable);

    EnrollmentResponse withdrawStudentFromCourse(UUID courseUuid, UUID studentUuid, EnrollmentRequest.Withdraw request);

    EnrollmentResponse blockEnrollment(UUID courseUuid, UUID studentUuid, EnrollmentRequest.Block request, Long blockedById);

    EnrollmentResponse unblockEnrollment(UUID courseUuid, UUID studentUuid);
}
