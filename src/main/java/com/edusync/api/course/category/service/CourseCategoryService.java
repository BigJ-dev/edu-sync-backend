package com.edusync.api.course.category.service;

import com.edusync.api.course.category.dto.CategoryRequest;
import com.edusync.api.course.category.dto.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CourseCategoryService {

    CategoryResponse createCourseCategory(CategoryRequest.Create request);

    List<CategoryResponse> findAllCourseCategories(Boolean active, String search);

    List<CategoryResponse> findRootCategories();

    List<CategoryResponse> findChildCategories(UUID parentUuid);

    CategoryResponse findCourseCategoryByUuid(UUID categoryUuid);

    CategoryResponse updateCourseCategory(UUID categoryUuid, CategoryRequest.Update request);

    void deleteCourseCategory(UUID categoryUuid);

    CategoryResponse assignCourseToCategory(UUID categoryUuid, CategoryRequest.AssignCourse request);

    void unassignCourseFromCategory(UUID categoryUuid, UUID courseUuid);

    List<CategoryResponse> findCategoriesByCourse(UUID courseUuid);
}
