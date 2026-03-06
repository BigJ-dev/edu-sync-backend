package com.edusync.api.course.certificate.repo;

import com.edusync.api.course.certificate.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {

    Optional<Certificate> findByUuid(UUID uuid);

    Optional<Certificate> findByCertificateNumber(String certificateNumber);

    Optional<Certificate> findByVerificationCode(String verificationCode);

    Optional<Certificate> findByCourseIdAndStudentId(Long courseId, Long studentId);

    boolean existsByEnrollmentId(Long enrollmentId);
}
