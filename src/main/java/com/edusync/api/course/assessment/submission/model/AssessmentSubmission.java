package com.edusync.api.course.assessment.submission.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.assessment.common.model.Assessment;
import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assessment_submission", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(columnDefinition = "TEXT")
    private String submissionText;

    @Column(length = 500)
    private String s3Key;

    @Column(length = 255)
    private String fileName;

    private Long fileSizeBytes;

    @Column(length = 100)
    private String mimeType;

    @Column(nullable = false)
    private Instant submittedAt;

    @Column(nullable = false)
    private boolean isLate;

    @Column(precision = 6, scale = 2)
    private BigDecimal marksObtained;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graded_by")
    private AppUser gradedBy;

    private Instant gradedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "submission_status")
    private SubmissionStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @PrePersist
    void defaults() {
        if (uuid == null) uuid = UUID.randomUUID();
        if (submittedAt == null) submittedAt = Instant.now();
    }
}
