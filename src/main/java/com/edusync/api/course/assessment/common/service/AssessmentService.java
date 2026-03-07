package com.edusync.api.course.assessment.common.service;

import com.edusync.api.course.assessment.common.dto.AssessmentRequest;
import com.edusync.api.course.assessment.common.dto.AssessmentResponse;
import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.common.enums.DeliveryMode;
import com.edusync.api.course.assessment.common.model.Assessment;

import java.util.List;
import java.util.UUID;

public interface AssessmentService {

    AssessmentResponse createAssessment(UUID moduleUuid, AssessmentRequest.Create request);

    List<AssessmentResponse> findAllAssessmentsByModule(UUID moduleUuid, AssessmentStatus status,
                                              AssessmentType type, DeliveryMode mode, String search);

    AssessmentResponse findAssessmentByUuid(UUID assessmentUuid);

    AssessmentResponse updateAssessment(UUID assessmentUuid, AssessmentRequest.Update request);

    AssessmentResponse updateAssessmentStatus(UUID assessmentUuid, AssessmentRequest.UpdateStatus request);

    Assessment findAssessmentEntityByUuid(UUID uuid);
}
