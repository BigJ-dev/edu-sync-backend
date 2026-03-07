package com.edusync.api.course.grading.repo;

import com.edusync.api.course.grading.model.GradeWeightCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GradeWeightCategoryRepository extends JpaRepository<GradeWeightCategory, Long> {

    List<GradeWeightCategory> findByCourseIdOrderBySortOrder(Long courseId);

    Optional<GradeWeightCategory> findByUuid(UUID uuid);
}
