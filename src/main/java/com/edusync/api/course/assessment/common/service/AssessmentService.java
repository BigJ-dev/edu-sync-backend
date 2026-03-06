package com.edusync.api.course.assessment.common.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.assessment.common.dto.AssessmentRequest;
import com.edusync.api.course.assessment.common.dto.AssessmentResponse;
import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.common.enums.DeliveryMode;
import com.edusync.api.course.assessment.common.model.Assessment;
import com.edusync.api.course.assessment.common.repo.AssessmentRepository;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.module.repo.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentService {

    private final AssessmentRepository repository;
    private final ModuleRepository moduleRepository;
    private final AppUserRepository appUserRepository;

    public AssessmentResponse create(UUID moduleUuid, AssessmentRequest.Create request) {
        var module = findModule(moduleUuid);
        var createdBy = findAppUser(request.createdByUuid());

        var assessment = Assessment.builder()
                .module(module)
                .createdBy(createdBy)
                .title(request.title())
                .description(request.description())
                .assessmentType(request.assessmentType())
                .deliveryMode(request.deliveryMode())
                .totalMarks(request.totalMarks())
                .weightPct(request.weightPct())
                .dueDate(request.dueDate())
                .visibleFrom(request.visibleFrom())
                .allowLateSubmission(request.allowLateSubmission() != null ? request.allowLateSubmission() : false)
                .latePenaltyPct(request.latePenaltyPct())
                .briefS3Key(request.briefS3Key())
                .briefFileName(request.briefFileName())
                .status(AssessmentStatus.DRAFT)
                .build();

        return AssessmentResponse.from(repository.save(assessment));
    }

    @Transactional(readOnly = true)
    public List<AssessmentResponse> findAllByModule(UUID moduleUuid, AssessmentStatus status,
                                                     AssessmentType type, DeliveryMode mode, String search) {
        var module = findModule(moduleUuid);
        var spec = Specification.where(AssessmentSpec.hasModuleId(module.getId()))
                .and(AssessmentSpec.hasStatus(status))
                .and(AssessmentSpec.hasAssessmentType(type))
                .and(AssessmentSpec.hasDeliveryMode(mode))
                .and(AssessmentSpec.searchByTitle(search));
        return repository.findAll(spec).stream().map(AssessmentResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AssessmentResponse findByUuid(UUID assessmentUuid) {
        return AssessmentResponse.from(findAssessment(assessmentUuid));
    }

    public AssessmentResponse update(UUID assessmentUuid, AssessmentRequest.Update request) {
        var assessment = findAssessment(assessmentUuid);
        assessment.setTitle(request.title());
        assessment.setDescription(request.description());
        assessment.setAssessmentType(request.assessmentType());
        assessment.setDeliveryMode(request.deliveryMode());
        assessment.setTotalMarks(request.totalMarks());
        assessment.setWeightPct(request.weightPct());
        assessment.setDueDate(request.dueDate());
        assessment.setVisibleFrom(request.visibleFrom());
        if (request.allowLateSubmission() != null) assessment.setAllowLateSubmission(request.allowLateSubmission());
        assessment.setLatePenaltyPct(request.latePenaltyPct());
        assessment.setBriefS3Key(request.briefS3Key());
        assessment.setBriefFileName(request.briefFileName());
        return AssessmentResponse.from(repository.save(assessment));
    }

    public AssessmentResponse updateStatus(UUID assessmentUuid, AssessmentRequest.UpdateStatus request) {
        var assessment = findAssessment(assessmentUuid);
        assessment.setStatus(request.status());
        return AssessmentResponse.from(repository.save(assessment));
    }

    public Assessment findAssessment(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Assessment was not found"));
    }

    private CourseModule findModule(UUID uuid) {
        return moduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }
}
