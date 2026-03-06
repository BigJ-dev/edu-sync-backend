package com.edusync.api.course.common.repo;

import com.edusync.api.course.common.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    Optional<Course> findByUuid(UUID uuid);

    boolean existsByCode(String code);
}
