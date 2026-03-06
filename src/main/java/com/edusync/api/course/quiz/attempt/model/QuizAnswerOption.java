package com.edusync.api.course.quiz.attempt.model;

import com.edusync.api.course.quiz.question.model.QuizQuestionOption;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quiz_answer_option", schema = "edusync")
@SuppressWarnings("JpaDataSourceORMInspection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizAnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", nullable = false)
    private QuizAnswer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private QuizQuestionOption option;
}
