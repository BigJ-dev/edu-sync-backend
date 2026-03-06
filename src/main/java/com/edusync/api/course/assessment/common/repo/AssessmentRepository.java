package com.edusync.api.course.assessment.common.repo;

import com.edusync.api.course.assessment.common.model.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AssessmentRepository extends JpaRepository<Assessment, Long>, JpaSpecificationExecutor<Assessment> {

    Optional<Assessment> findByUuid(UUID uuid);
}
