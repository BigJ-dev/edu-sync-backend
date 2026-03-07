package com.edusync.api.course.support.repo;

import com.edusync.api.course.support.model.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long>, JpaSpecificationExecutor<SupportMessage> {

    Optional<SupportMessage> findByUuid(UUID uuid);

    List<SupportMessage> findByThreadIdOrderByCreatedAtAsc(Long threadId);
}
