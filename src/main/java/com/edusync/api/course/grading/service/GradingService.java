package com.edusync.api.course.grading.service;

import com.edusync.api.course.grading.dto.FinalGradeResponse;
import com.edusync.api.course.grading.dto.GradeWeightRequest;
import com.edusync.api.course.grading.dto.GradeWeightResponse;

import java.util.List;
import java.util.UUID;

public interface GradingService {

    GradeWeightResponse createCategory(UUID courseUuid, GradeWeightRequest.Create request);

    GradeWeightResponse updateCategory(UUID categoryUuid, GradeWeightRequest.Update request);

    void deleteCategory(UUID categoryUuid);

    List<GradeWeightResponse> findCategoriesByCourse(UUID courseUuid);

    FinalGradeResponse calculateFinalGrade(UUID courseUuid, UUID studentUuid);
}
