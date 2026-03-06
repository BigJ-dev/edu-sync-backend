package com.edusync.api.course.quiz.common.model;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "quiz", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private CourseModule module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_session_id")
    private ClassSession classSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private AppUser createdBy;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer timeLimitMinutes;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal totalMarks;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal passMarkPct;

    @Column(precision = 5, scale = 2)
    private BigDecimal weightPct;

    @Column(nullable = false)
    private int maxAttempts;

    @Column(nullable = false)
    private boolean shuffleQuestions;

    @Column(nullable = false)
    private boolean showAnswersAfter;

    @Column(length = 500)
    private String documentS3Key;

    @Column(length = 255)
    private String documentName;

    private Instant visibleFrom;

    private Instant visibleUntil;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "quiz_status")
    private QuizStatus status;

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
