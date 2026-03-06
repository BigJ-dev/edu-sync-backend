package com.edusync.api.course.assessment.common.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.common.enums.DeliveryMode;
import com.edusync.api.course.module.model.CourseModule;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "assessment", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "assessment_type")
    private AssessmentType assessmentType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "delivery_mode")
    private DeliveryMode deliveryMode;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal totalMarks;

    @Column(precision = 5, scale = 2)
    private BigDecimal weightPct;

    @Column(nullable = false)
    private Instant dueDate;

    private Instant visibleFrom;

    @Column(nullable = false)
    private boolean allowLateSubmission;

    @Column(precision = 5, scale = 2)
    private BigDecimal latePenaltyPct;

    @Column(length = 500)
    private String briefS3Key;

    @Column(length = 255)
    private String briefFileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "assessment_status")
    private AssessmentStatus status;

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
