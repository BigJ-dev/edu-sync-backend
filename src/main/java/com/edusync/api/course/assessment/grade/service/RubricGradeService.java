package com.edusync.api.course.assessment.grade.service;

import com.edusync.api.course.assessment.grade.dto.RubricGradeRequest;
import com.edusync.api.course.assessment.grade.dto.RubricGradeResponse;

import java.util.List;
import java.util.UUID;

public interface RubricGradeService {

    RubricGradeResponse awardRubricGrade(UUID submissionUuid, UUID criteriaUuid, RubricGradeRequest.Award request);

    List<RubricGradeResponse> findAllRubricGradesBySubmission(UUID submissionUuid);
}
