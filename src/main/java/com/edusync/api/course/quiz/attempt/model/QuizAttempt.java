package com.edusync.api.course.quiz.attempt.model;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.quiz.attempt.enums.AttemptStatus;
import com.edusync.api.course.quiz.common.model.Quiz;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "quiz_attempt", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private int attemptNumber;

    @Column(nullable = false)
    private Instant startedAt;

    private Instant completedAt;

    @Column(precision = 6, scale = 2)
    private BigDecimal score;

    @Column(precision = 5, scale = 2)
    private BigDecimal scorePct;

    private Boolean passed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "quiz_attempt_status")
    private AttemptStatus status;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @PrePersist
    void defaults() {
        if (uuid == null) uuid = UUID.randomUUID();
        if (startedAt == null) startedAt = Instant.now();
    }
}
