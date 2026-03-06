package com.edusync.api.course.material.repo;

import com.edusync.api.course.material.model.StudyMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface MaterialRepository extends JpaRepository<StudyMaterial, Long>, JpaSpecificationExecutor<StudyMaterial> {

    Optional<StudyMaterial> findByUuid(UUID uuid);
}
