package com.edusync.api.course.grading.controller;

import com.edusync.api.course.grading.controller.api.GradingApi;
import com.edusync.api.course.grading.dto.FinalGradeResponse;
import com.edusync.api.course.grading.dto.GradeWeightRequest;
import com.edusync.api.course.grading.dto.GradeWeightResponse;
import com.edusync.api.course.grading.service.GradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GradingController implements GradingApi {

    private final GradingService service;

    @Override
    public GradeWeightResponse createCategory(UUID courseUuid, GradeWeightRequest.Create request) {
        return service.createCategory(courseUuid, request);
    }

    @Override
    public GradeWeightResponse updateCategory(UUID courseUuid, UUID categoryUuid, GradeWeightRequest.Update request) {
        return service.updateCategory(categoryUuid, request);
    }

    @Override
    public void deleteCategory(UUID courseUuid, UUID categoryUuid) {
        service.deleteCategory(categoryUuid);
    }

    @Override
    public List<GradeWeightResponse> findCategories(UUID courseUuid) {
        return service.findCategoriesByCourse(courseUuid);
    }

    @Override
    public FinalGradeResponse calculateFinalGrade(UUID courseUuid, UUID studentUuid) {
        return service.calculateFinalGrade(courseUuid, studentUuid);
    }
}
