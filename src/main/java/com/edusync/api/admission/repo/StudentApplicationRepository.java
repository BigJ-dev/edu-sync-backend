package com.edusync.api.admission.repo;

import com.edusync.api.admission.model.StudentApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface StudentApplicationRepository extends JpaRepository<StudentApplication, Long>,
        JpaSpecificationExecutor<StudentApplication> {

    Optional<StudentApplication> findByUuid(UUID uuid);

    boolean existsByEmail(String email);

    boolean existsByIdNumber(String idNumber);

    @Query("SELECT COALESCE(MAX(s.id), 0) FROM Student s")
    long findMaxStudentId();
}
