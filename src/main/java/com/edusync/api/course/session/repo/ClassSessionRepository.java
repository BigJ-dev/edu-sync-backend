package com.edusync.api.course.session.repo;

import com.edusync.api.course.session.model.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long>, JpaSpecificationExecutor<ClassSession> {

    Optional<ClassSession> findByUuid(UUID uuid);

    boolean existsByModuleIdAndSessionNumber(Long moduleId, int sessionNumber);
}
