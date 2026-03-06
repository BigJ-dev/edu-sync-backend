package com.edusync.api.course.quiz.attempt.model;

import com.edusync.api.course.quiz.question.model.QuizQuestion;
import com.edusync.api.course.quiz.question.model.QuizQuestionOption;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "quiz_answer", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_attempt_id", nullable = false)
    private QuizAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private QuizQuestion question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private QuizQuestionOption selectedOption;

    @Column(length = 2000)
    private String answerText;

    private Boolean isCorrect;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal marksAwarded;

    @Column(nullable = false)
    private boolean requiresManualGrading;

    @Column(nullable = false)
    private Instant answeredAt;

    @PrePersist
    void defaults() {
        if (answeredAt == null) answeredAt = Instant.now();
        if (marksAwarded == null) marksAwarded = BigDecimal.ZERO;
    }
}
