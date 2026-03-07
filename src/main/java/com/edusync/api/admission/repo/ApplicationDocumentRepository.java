package com.edusync.api.admission.repo;

import com.edusync.api.admission.model.ApplicationDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, Long> {

    List<ApplicationDocument> findByApplicationId(Long applicationId);

    Optional<ApplicationDocument> findByUuid(UUID uuid);
}
