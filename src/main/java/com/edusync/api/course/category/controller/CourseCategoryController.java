package com.edusync.api.course.category.controller;

import com.edusync.api.course.category.controller.api.CourseCategoryApi;
import com.edusync.api.course.category.dto.CategoryRequest;
import com.edusync.api.course.category.dto.CategoryResponse;
import com.edusync.api.course.category.service.CourseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class CourseCategoryController implements CourseCategoryApi {

    private final CourseCategoryService service;

    @Override
    public CategoryResponse create(CategoryRequest.Create request) {
        return service.create(request);
    }

    @Override
    public List<CategoryResponse> findAll(Boolean active, String search) {
        return service.findAll(active, search);
    }

    @Override
    public List<CategoryResponse> findRoots() {
        return service.findRoots();
    }

    @Override
    public CategoryResponse findByUuid(UUID categoryUuid) {
        return service.findByUuid(categoryUuid);
    }

    @Override
    public List<CategoryResponse> findChildren(UUID categoryUuid) {
        return service.findChildren(categoryUuid);
    }

    @Override
    public CategoryResponse update(UUID categoryUuid, CategoryRequest.Update request) {
        return service.update(categoryUuid, request);
    }

    @Override
    public void delete(UUID categoryUuid) {
        service.delete(categoryUuid);
    }

    @Override
    public CategoryResponse assignCourse(UUID categoryUuid, CategoryRequest.AssignCourse request) {
        return service.assignCourse(categoryUuid, request);
    }

    @Override
    public void unassignCourse(UUID categoryUuid, UUID courseUuid) {
        service.unassignCourse(categoryUuid, courseUuid);
    }

    @Override
    public List<CategoryResponse> findCourseCategories(UUID courseUuid) {
        return service.findCourseCategories(courseUuid);
    }
}
