package com.edusync.api.course.assessment.grade.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.assessment.grade.dto.RubricGradeRequest;
import com.edusync.api.course.assessment.grade.dto.RubricGradeResponse;
import com.edusync.api.course.assessment.grade.model.RubricGrade;
import com.edusync.api.course.assessment.grade.repo.RubricGradeRepository;
import com.edusync.api.course.assessment.rubric.model.RubricCriteria;
import com.edusync.api.course.assessment.rubric.service.RubricCriteriaService;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import com.edusync.api.course.assessment.submission.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RubricGradeService {

    private final RubricGradeRepository repository;
    private final SubmissionService submissionService;
    private final RubricCriteriaService rubricCriteriaService;
    private final AppUserRepository appUserRepository;

    public RubricGradeResponse award(UUID submissionUuid, UUID criteriaUuid, RubricGradeRequest.Award request) {
        var submission = submissionService.findSubmission(submissionUuid);
        var criteria = rubricCriteriaService.findCriteria(criteriaUuid);
        var gradedBy = findAppUser(request.gradedByUuid());

        var existing = repository.findBySubmissionIdAndCriteriaId(submission.getId(), criteria.getId());
        if (existing.isPresent()) {
            var grade = existing.get();
            grade.setMarksAwarded(request.marksAwarded());
            grade.setComment(request.comment());
            grade.setGradedBy(gradedBy);
            return RubricGradeResponse.from(repository.save(grade));
        }

        var grade = RubricGrade.builder()
                .submission(submission)
                .criteria(criteria)
                .marksAwarded(request.marksAwarded())
                .comment(request.comment())
                .gradedBy(gradedBy)
                .build();

        return RubricGradeResponse.from(repository.save(grade));
    }

    @Transactional(readOnly = true)
    public List<RubricGradeResponse> findAllBySubmission(UUID submissionUuid) {
        var submission = submissionService.findSubmission(submissionUuid);
        return repository.findBySubmissionId(submission.getId())
                .stream().map(RubricGradeResponse::from).toList();
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }
}
