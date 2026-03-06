package com.edusync.api.course.certificate.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.certificate.enums.CertificateStatus;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "certificate", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false, unique = true)
    private CourseEnrollment enrollment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issued_by", nullable = false)
    private AppUser issuedBy;

    @Column(nullable = false, unique = true, length = 50)
    private String certificateNumber;

    @Column(nullable = false, unique = true, length = 100)
    private String verificationCode;

    @Column(precision = 5, scale = 2)
    private BigDecimal finalGrade;

    @Column(precision = 5, scale = 2)
    private BigDecimal finalAttendancePct;

    @Column(nullable = false)
    private LocalDate completionDate;

    @Column(length = 500)
    private String s3Key;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "certificate_status")
    private CertificateStatus status;

    private Instant issuedAt;

    private Instant revokedAt;

    @Column(length = 500)
    private String revocationReason;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    void generateUuid() {
        if (uuid == null) uuid = UUID.randomUUID();
    }
}
