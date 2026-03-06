package com.edusync.api.course.messaging.repo;

import com.edusync.api.course.messaging.model.MessageThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface MessageThreadRepository extends JpaRepository<MessageThread, Long>, JpaSpecificationExecutor<MessageThread> {

    Optional<MessageThread> findByUuid(UUID uuid);
}
