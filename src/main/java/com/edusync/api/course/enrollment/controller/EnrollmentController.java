package com.edusync.api.course.enrollment.controller;

import com.edusync.api.course.enrollment.controller.api.EnrollmentApi;
import com.edusync.api.course.enrollment.dto.EnrollmentRequest;
import com.edusync.api.course.enrollment.dto.EnrollmentResponse;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class EnrollmentController implements EnrollmentApi {

    private final EnrollmentService service;

    @Override
    public EnrollmentResponse enroll(UUID courseUuid, EnrollmentRequest.Enroll request) {
        return service.enrollStudentInCourse(courseUuid, request);
    }

    @Override
    public Page<EnrollmentResponse> findAllByCourse(UUID courseUuid, EnrollmentStatus status, Boolean blocked, Pageable pageable) {
        return service.findAllEnrollmentsByCourse(courseUuid, status, blocked, pageable);
    }

    @Override
    public EnrollmentResponse withdraw(UUID courseUuid, UUID studentUuid, EnrollmentRequest.Withdraw request) {
        return service.withdrawStudentFromCourse(courseUuid, studentUuid, request);
    }

    @Override
    public EnrollmentResponse block(UUID courseUuid, UUID studentUuid, EnrollmentRequest.Block request) {
        return service.blockEnrollment(courseUuid, studentUuid, request, 1L);
    }

    @Override
    public EnrollmentResponse unblock(UUID courseUuid, UUID studentUuid) {
        return service.unblockEnrollment(courseUuid, studentUuid);
    }
}
