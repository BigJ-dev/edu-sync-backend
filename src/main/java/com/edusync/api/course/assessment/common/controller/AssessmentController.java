package com.edusync.api.course.assessment.common.controller;

import com.edusync.api.course.assessment.common.controller.api.AssessmentApi;
import com.edusync.api.course.assessment.common.dto.AssessmentRequest;
import com.edusync.api.course.assessment.common.dto.AssessmentResponse;
import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.common.enums.DeliveryMode;
import com.edusync.api.course.assessment.common.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class AssessmentController implements AssessmentApi {

    private final AssessmentService service;

    @Override
    public AssessmentResponse create(UUID moduleUuid, AssessmentRequest.Create request) {
        return service.create(moduleUuid, request);
    }

    @Override
    public List<AssessmentResponse> findAllByModule(UUID moduleUuid, AssessmentStatus status,
                                                     AssessmentType type, DeliveryMode mode, String search) {
        return service.findAllByModule(moduleUuid, status, type, mode, search);
    }

    @Override
    public AssessmentResponse findByUuid(UUID moduleUuid, UUID assessmentUuid) {
        return service.findByUuid(assessmentUuid);
    }

    @Override
    public AssessmentResponse update(UUID moduleUuid, UUID assessmentUuid, AssessmentRequest.Update request) {
        return service.update(assessmentUuid, request);
    }

    @Override
    public AssessmentResponse updateStatus(UUID moduleUuid, UUID assessmentUuid, AssessmentRequest.UpdateStatus request) {
        return service.updateStatus(assessmentUuid, request);
    }
}
