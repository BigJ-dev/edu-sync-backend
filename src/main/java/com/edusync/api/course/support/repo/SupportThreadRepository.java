package com.edusync.api.course.support.repo;

import com.edusync.api.course.support.model.SupportThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface SupportThreadRepository extends JpaRepository<SupportThread, Long>, JpaSpecificationExecutor<SupportThread> {

    Optional<SupportThread> findByUuid(UUID uuid);
}
