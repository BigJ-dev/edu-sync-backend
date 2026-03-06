package com.edusync.api.course.assessment.rubric.service;

import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.assessment.common.model.Assessment;
import com.edusync.api.course.assessment.common.service.AssessmentService;
import com.edusync.api.course.assessment.rubric.dto.RubricCriteriaRequest;
import com.edusync.api.course.assessment.rubric.dto.RubricCriteriaResponse;
import com.edusync.api.course.assessment.rubric.model.RubricCriteria;
import com.edusync.api.course.assessment.rubric.repo.RubricCriteriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RubricCriteriaService {

    private final RubricCriteriaRepository repository;
    private final AssessmentService assessmentService;

    public RubricCriteriaResponse create(UUID assessmentUuid, RubricCriteriaRequest.Create request) {
        var assessment = assessmentService.findAssessment(assessmentUuid);
        validateSortOrderUniqueness(assessment.getId(), request.sortOrder());

        var criteria = RubricCriteria.builder()
                .assessment(assessment)
                .title(request.title())
                .description(request.description())
                .maxMarks(request.maxMarks())
                .sortOrder(request.sortOrder())
                .build();

        return RubricCriteriaResponse.from(repository.save(criteria));
    }

    @Transactional(readOnly = true)
    public List<RubricCriteriaResponse> findAllByAssessment(UUID assessmentUuid) {
        var assessment = assessmentService.findAssessment(assessmentUuid);
        return repository.findByAssessmentIdOrderBySortOrder(assessment.getId())
                .stream().map(RubricCriteriaResponse::from).toList();
    }

    public RubricCriteriaResponse update(UUID criteriaUuid, RubricCriteriaRequest.Update request) {
        var criteria = findCriteria(criteriaUuid);

        if (criteria.getSortOrder() != request.sortOrder()) {
            validateSortOrderUniqueness(criteria.getAssessment().getId(), request.sortOrder());
        }

        criteria.setTitle(request.title());
        criteria.setDescription(request.description());
        criteria.setMaxMarks(request.maxMarks());
        criteria.setSortOrder(request.sortOrder());
        return RubricCriteriaResponse.from(repository.save(criteria));
    }

    public void delete(UUID criteriaUuid) {
        var criteria = findCriteria(criteriaUuid);
        repository.delete(criteria);
    }

    public RubricCriteria findCriteria(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Rubric criteria was not found"));
    }

    private void validateSortOrderUniqueness(Long assessmentId, int sortOrder) {
        if (repository.existsByAssessmentIdAndSortOrder(assessmentId, sortOrder))
            throw new ServiceException(HttpStatus.CONFLICT, "A rubric criteria with this sort order already exists");
    }
}
