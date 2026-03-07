package com.edusync.api.broadcast.repo;

import com.edusync.api.broadcast.model.BroadcastRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BroadcastRecipientRepository extends JpaRepository<BroadcastRecipient, Long> {

    List<BroadcastRecipient> findByBroadcastMessageId(Long broadcastMessageId);

    Optional<BroadcastRecipient> findByBroadcastMessageIdAndStudentId(Long broadcastMessageId, Long studentId);

    boolean existsByBroadcastMessageIdAndStudentId(Long broadcastMessageId, Long studentId);
}
