package com.edusync.api.course.messaging.repo;

import com.edusync.api.course.messaging.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {

    Optional<Message> findByUuid(UUID uuid);

    List<Message> findByThreadIdOrderByCreatedAtAsc(Long threadId);
}
