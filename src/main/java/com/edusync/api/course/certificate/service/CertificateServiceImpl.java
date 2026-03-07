package com.edusync.api.course.certificate.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.certificate.dto.CertificateRequest;
import com.edusync.api.course.certificate.dto.CertificateResponse;
import com.edusync.api.course.certificate.enums.CertificateStatus;
import com.edusync.api.course.certificate.model.Certificate;
import com.edusync.api.course.certificate.repo.CertificateRepository;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CertificateServiceImpl implements CertificateService {

    private final CertificateRepository repository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public CertificateResponse issueCertificate(CertificateRequest.Issue request) {
        var course = findCourse(request.courseUuid());
        var student = findStudent(request.studentUuid());
        var issuedBy = findAppUser(request.issuedByUuid());

        var enrollment = enrollmentRepository.findByCourseIdAndStudentId(course.getId(), student.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Enrollment was not found"));

        if (enrollment.getStatus() != EnrollmentStatus.COMPLETED) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Enrollment must be in COMPLETED status to issue a certificate");
        }

        if (repository.existsByEnrollmentId(enrollment.getId())) {
            throw new ServiceException(HttpStatus.CONFLICT, "Certificate has already been issued for this enrollment");
        }

        var certificateNumber = "EDUSYNC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        var verificationCode = UUID.randomUUID().toString();

        var certificate = Certificate.builder()
                .course(course)
                .student(student)
                .enrollment(enrollment)
                .issuedBy(issuedBy)
                .certificateNumber(certificateNumber)
                .verificationCode(verificationCode)
                .finalGrade(request.finalGrade())
                .finalAttendancePct(request.finalAttendancePct())
                .completionDate(request.completionDate())
                .s3Key(request.s3Key())
                .status(CertificateStatus.ISSUED)
                .issuedAt(Instant.now())
                .build();

        return CertificateResponse.from(repository.save(certificate));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateResponse> findAllCertificates(UUID courseUuid, CertificateStatus status, String search) {
        Long courseId = null;
        if (courseUuid != null) {
            courseId = findCourse(courseUuid).getId();
        }

        var spec = Specification.where(CertificateSpec.hasCourseId(courseId))
                .and(CertificateSpec.hasStatus(status))
                .and(CertificateSpec.searchByCertificateNumber(search));

        return repository.findAll(spec).stream()
                .map(CertificateResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateResponse findCertificateByUuid(UUID uuid) {
        var certificate = repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Certificate was not found"));
        return CertificateResponse.from(certificate);
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateResponse verifyCertificate(String verificationCode) {
        var certificate = repository.findByVerificationCode(verificationCode)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Certificate was not found"));
        return CertificateResponse.from(certificate);
    }

    @Override
    public CertificateResponse revokeCertificate(UUID certificateUuid, CertificateRequest.Revoke request) {
        var certificate = repository.findByUuid(certificateUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Certificate was not found"));

        certificate.setStatus(CertificateStatus.REVOKED);
        certificate.setRevokedAt(Instant.now());
        certificate.setRevocationReason(request.revocationReason());

        return CertificateResponse.from(repository.save(certificate));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CertificateResponse> findCertificatesByStudent(UUID studentUuid) {
        var student = findStudent(studentUuid);

        var spec = Specification.where(CertificateSpec.hasStudentId(student.getId()));

        return repository.findAll(spec).stream()
                .map(CertificateResponse::from)
                .toList();
    }

    private Course findCourse(UUID courseUuid) {
        return courseRepository.findByUuid(courseUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }

    private Student findStudent(UUID studentUuid) {
        return studentRepository.findByUuid(studentUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }

    private AppUser findAppUser(UUID userUuid) {
        return appUserRepository.findByUuid(userUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }
}
