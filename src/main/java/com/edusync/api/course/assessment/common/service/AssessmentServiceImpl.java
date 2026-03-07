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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Transactional
public class AssessmentServiceImpl implements AssessmentService {

    private final AssessmentRepository repository;
    private final ModuleRepository moduleRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public AssessmentResponse createAssessment(UUID moduleUuid, AssessmentRequest.Create request) {
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
                .allowLateSubmission(Objects.requireNonNullElse(request.allowLateSubmission(), false))
                .latePenaltyPct(request.latePenaltyPct())
                .briefS3Key(request.briefS3Key())
                .briefFileName(request.briefFileName())
                .status(AssessmentStatus.DRAFT)
                .build();

        return AssessmentResponse.from(repository.save(assessment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findAllAssessmentsByModule(UUID moduleUuid, AssessmentStatus status,
                                                               AssessmentType type, DeliveryMode mode, String search) {
        var module = findModule(moduleUuid);

        var spec = Specification.where(AssessmentSpec.hasModuleId(module.getId()))
                .and(AssessmentSpec.hasStatus(status))
                .and(AssessmentSpec.hasAssessmentType(type))
                .and(AssessmentSpec.hasDeliveryMode(mode))
                .and(AssessmentSpec.searchByTitle(search));

        return repository.findAll(spec).stream()
                .map(AssessmentResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AssessmentResponse findAssessmentByUuid(UUID assessmentUuid) {
        return AssessmentResponse.from(findAssessmentEntityByUuid(assessmentUuid));
    }

    @Override
    public AssessmentResponse updateAssessment(UUID assessmentUuid, AssessmentRequest.Update request) {
        var assessment = findAssessmentEntityByUuid(assessmentUuid);

        assessment.setTitle(request.title());
        assessment.setDescription(request.description());
        assessment.setAssessmentType(request.assessmentType());
        assessment.setDeliveryMode(request.deliveryMode());
        assessment.setTotalMarks(request.totalMarks());
        assessment.setWeightPct(request.weightPct());
        assessment.setDueDate(request.dueDate());
        assessment.setVisibleFrom(request.visibleFrom());
        assessment.setLatePenaltyPct(request.latePenaltyPct());
        assessment.setBriefS3Key(request.briefS3Key());
        assessment.setBriefFileName(request.briefFileName());

        Optional.ofNullable(request.allowLateSubmission()).ifPresent(assessment::setAllowLateSubmission);

        return AssessmentResponse.from(repository.save(assessment));
    }

    @Override
    public AssessmentResponse updateAssessmentStatus(UUID assessmentUuid, AssessmentRequest.UpdateStatus request) {
        var assessment = findAssessmentEntityByUuid(assessmentUuid);
        assessment.setStatus(request.status());
        return AssessmentResponse.from(repository.save(assessment));
    }

    @Override
    public void deleteAssessment(UUID assessmentUuid) {
        repository.delete(findAssessmentEntityByUuid(assessmentUuid));
    }

    @Override
    public AssessmentResponse duplicateAssessment(UUID assessmentUuid, UUID targetModuleUuid) {
        var source = findAssessmentEntityByUuid(assessmentUuid);
        var targetModule = findModule(targetModuleUuid);

        var copy = Assessment.builder()
                .module(targetModule)
                .createdBy(source.getCreatedBy())
                .title(source.getTitle() + " (Copy)")
                .description(source.getDescription())
                .assessmentType(source.getAssessmentType())
                .deliveryMode(source.getDeliveryMode())
                .totalMarks(source.getTotalMarks())
                .weightPct(source.getWeightPct())
                .dueDate(source.getDueDate())
                .visibleFrom(source.getVisibleFrom())
                .allowLateSubmission(source.isAllowLateSubmission())
                .latePenaltyPct(source.getLatePenaltyPct())
                .briefS3Key(source.getBriefS3Key())
                .briefFileName(source.getBriefFileName())
                .status(AssessmentStatus.DRAFT)
                .build();

        return AssessmentResponse.from(repository.save(copy));
    }

    @Override
    public AssessmentResponse reopenAssessment(UUID assessmentUuid, AssessmentRequest.Reopen request) {
        var assessment = findAssessmentEntityByUuid(assessmentUuid);

        assessment.setStatus(AssessmentStatus.PUBLISHED);
        assessment.setDueDate(request.newDueDate());

        Optional.ofNullable(request.visibleFrom()).ifPresent(assessment::setVisibleFrom);
        Optional.ofNullable(request.allowLateSubmission()).ifPresent(assessment::setAllowLateSubmission);

        return AssessmentResponse.from(repository.save(assessment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssessmentResponse> findAllByLecturer(UUID lecturerUuid, AssessmentStatus status, String search) {
        var lecturer = findAppUser(lecturerUuid);

        Predicate<Assessment> statusFilter = a -> Objects.isNull(status) || a.getStatus() == status;
        Predicate<Assessment> searchFilter = a -> Objects.isNull(search) || search.isBlank()
                || a.getTitle().toLowerCase().contains(search.toLowerCase());

        return repository.findByCreatedById(lecturer.getId()).stream()
                .filter(statusFilter.and(searchFilter))
                .map(AssessmentResponse::from)
                .toList();
    }

    @Override
    public Assessment findAssessmentEntityByUuid(UUID uuid) {
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
