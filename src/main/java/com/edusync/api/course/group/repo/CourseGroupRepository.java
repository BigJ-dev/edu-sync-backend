package com.edusync.api.course.group.repo;

import com.edusync.api.course.group.model.CourseGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseGroupRepository extends JpaRepository<CourseGroup, Long>, JpaSpecificationExecutor<CourseGroup> {

    Optional<CourseGroup> findByUuid(UUID uuid);

    List<CourseGroup> findByCourseIdOrderByNameAsc(Long courseId);
}
