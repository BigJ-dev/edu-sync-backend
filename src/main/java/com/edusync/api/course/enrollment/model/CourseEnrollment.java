package com.edusync.api.course.enrollment.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "course_enrollment", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private Instant enrolledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enrollment_status")
    private EnrollmentStatus status;

    @Column(precision = 5, scale = 2)
    private BigDecimal finalAttendancePct;

    @Column(precision = 5, scale = 2)
    private BigDecimal finalGrade;

    private Instant withdrawnAt;

    @Column(length = 500)
    private String withdrawalReason;

    @Column(name = "is_blocked", nullable = false)
    private boolean blocked;

    private Instant blockedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_by")
    private AppUser blockedBy;

    @Column(length = 500)
    private String blockedReason;

    @PrePersist
    void setDefaults() {
        if (enrolledAt == null) enrolledAt = Instant.now();
        if (status == null) status = EnrollmentStatus.ENROLLED;
    }
}
