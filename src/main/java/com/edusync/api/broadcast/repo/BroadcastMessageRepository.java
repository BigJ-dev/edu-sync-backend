package com.edusync.api.broadcast.repo;

import com.edusync.api.broadcast.model.BroadcastMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface BroadcastMessageRepository extends JpaRepository<BroadcastMessage, Long>, JpaSpecificationExecutor<BroadcastMessage> {

    Optional<BroadcastMessage> findByUuid(UUID uuid);
}
