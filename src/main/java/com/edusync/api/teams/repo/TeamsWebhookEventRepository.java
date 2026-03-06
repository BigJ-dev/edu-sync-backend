package com.edusync.api.teams.repo;

import com.edusync.api.teams.model.TeamsWebhookEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamsWebhookEventRepository extends JpaRepository<TeamsWebhookEvent, Long> {

    Optional<TeamsWebhookEvent> findByEventId(String eventId);

    List<TeamsWebhookEvent> findByProcessedFalseOrderByCreatedAtAsc();
}
