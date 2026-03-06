package com.edusync.api.course.category.repo;

import com.edusync.api.course.category.model.CourseCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseCategoryMappingRepository extends JpaRepository<CourseCategoryMapping, Long> {

    List<CourseCategoryMapping> findByCourseId(Long courseId);

    List<CourseCategoryMapping> findByCategoryId(Long categoryId);

    boolean existsByCourseIdAndCategoryId(Long courseId, Long categoryId);

    void deleteByCourseIdAndCategoryId(Long courseId, Long categoryId);
}
