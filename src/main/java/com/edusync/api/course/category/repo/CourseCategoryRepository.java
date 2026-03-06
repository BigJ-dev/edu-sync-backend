package com.edusync.api.course.category.repo;

import com.edusync.api.course.category.model.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long>, JpaSpecificationExecutor<CourseCategory> {

    Optional<CourseCategory> findByUuid(UUID uuid);

    List<CourseCategory> findByParentIdIsNullOrderBySortOrderAsc();

    List<CourseCategory> findByParentIdOrderBySortOrderAsc(Long parentId);

    boolean existsByNameAndParentId(String name, Long parentId);
}
