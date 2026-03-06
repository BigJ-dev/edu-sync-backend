package com.edusync.api.course.module.repo;

import com.edusync.api.course.module.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<CourseModule, Long>, JpaSpecificationExecutor<CourseModule> {

    Optional<CourseModule> findByUuid(UUID uuid);

    List<CourseModule> findByCourseIdOrderBySortOrder(Long courseId);

    boolean existsByCourseIdAndSortOrder(Long courseId, int sortOrder);
}
