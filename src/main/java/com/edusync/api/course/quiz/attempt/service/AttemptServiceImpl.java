package com.edusync.api.course.quiz.attempt.service;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.quiz.attempt.dto.AnswerResponse;
import com.edusync.api.course.quiz.attempt.dto.AttemptRequest;
import com.edusync.api.course.quiz.attempt.dto.AttemptResponse;
import com.edusync.api.course.quiz.attempt.enums.AttemptStatus;
import com.edusync.api.course.quiz.attempt.model.QuizAnswer;
import com.edusync.api.course.quiz.attempt.model.QuizAnswerOption;
import com.edusync.api.course.quiz.attempt.model.QuizAttempt;
import com.edusync.api.course.quiz.attempt.repo.AnswerOptionRepository;
import com.edusync.api.course.quiz.attempt.repo.AnswerRepository;
import com.edusync.api.course.quiz.attempt.repo.AttemptRepository;
import com.edusync.api.course.quiz.common.model.Quiz;
import com.edusync.api.course.quiz.common.service.QuizService;
import com.edusync.api.course.quiz.question.enums.QuestionType;
import com.edusync.api.course.quiz.question.model.QuizQuestion;
import com.edusync.api.course.quiz.question.model.QuizQuestionOption;
import com.edusync.api.course.quiz.question.repo.QuestionOptionRepository;
import com.edusync.api.course.quiz.question.repo.QuestionRepository;
import com.edusync.api.course.quiz.question.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AttemptServiceImpl implements AttemptService {

    private final AttemptRepository attemptRepository;
    private final AnswerRepository answerRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final QuizService quizService;
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final StudentRepository studentRepository;

    @Override
    public AttemptResponse startQuizAttempt(UUID quizUuid, AttemptRequest.Start request) {
        var quiz = quizService.findQuizEntityByUuid(quizUuid);
        var student = findStudent(request.studentUuid());

        long existingAttempts = attemptRepository.countByQuizIdAndStudentId(quiz.getId(), student.getId());
        if (existingAttempts >= quiz.getMaxAttempts()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Maximum number of attempts reached for this quiz");
        }

        var attempt = QuizAttempt.builder()
                .quiz(quiz)
                .student(student)
                .attemptNumber((int) existingAttempts + 1)
                .status(AttemptStatus.IN_PROGRESS)
                .build();

        return AttemptResponse.from(attemptRepository.save(attempt));
    }

    @Override
    public AnswerResponse submitQuizAnswer(UUID attemptUuid, AttemptRequest.SubmitAnswer request) {
        var attempt = findQuizAttemptEntityByUuid(attemptUuid);

        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Cannot submit answers to a completed or timed out attempt");
        }

        var question = questionService.findQuizQuestionEntityByUuid(request.questionUuid());

        QuizQuestionOption selectedOption = null;
        if (request.selectedOptionUuid() != null) {
            selectedOption = findOption(request.selectedOptionUuid());
        }

        boolean requiresManualGrading = false;
        Boolean isCorrect = null;
        BigDecimal marksAwarded = BigDecimal.ZERO;

        if (question.getQuestionType() == QuestionType.MULTIPLE_CHOICE || question.getQuestionType() == QuestionType.TRUE_FALSE) {
            if (selectedOption != null) {
                isCorrect = selectedOption.isCorrect();
                marksAwarded = isCorrect ? question.getMarks() : BigDecimal.ZERO;
            } else {
                isCorrect = false;
            }
        } else if (question.getQuestionType() == QuestionType.MULTI_SELECT) {
            var correctOptionIds = questionOptionRepository.findByQuestionIdOrderBySortOrderAsc(question.getId())
                    .stream().filter(QuizQuestionOption::isCorrect)
                    .map(QuizQuestionOption::getId)
                    .collect(Collectors.toSet());

            Set<Long> selectedOptionIds = Set.of();
            if (request.selectedOptionUuids() != null && !request.selectedOptionUuids().isEmpty()) {
                selectedOptionIds = request.selectedOptionUuids().stream()
                        .map(uuid -> findOption(uuid).getId())
                        .collect(Collectors.toSet());
            }

            isCorrect = correctOptionIds.equals(selectedOptionIds);
            marksAwarded = isCorrect ? question.getMarks() : BigDecimal.ZERO;
        } else if (question.getQuestionType() == QuestionType.SHORT_ANSWER) {
            requiresManualGrading = true;
        }

        var answer = QuizAnswer.builder()
                .attempt(attempt)
                .question(question)
                .selectedOption(selectedOption)
                .answerText(request.answerText())
                .isCorrect(isCorrect)
                .marksAwarded(marksAwarded)
                .requiresManualGrading(requiresManualGrading)
                .build();

        answer = answerRepository.save(answer);

        // Save multi-select answer options
        List<QuizAnswerOption> answerOptions = List.of();
        if (question.getQuestionType() == QuestionType.MULTI_SELECT && request.selectedOptionUuids() != null) {
            final QuizAnswer savedAnswer = answer;
            answerOptions = request.selectedOptionUuids().stream().map(uuid -> {
                var option = findOption(uuid);
                var answerOption = QuizAnswerOption.builder()
                        .answer(savedAnswer)
                        .option(option)
                        .build();
                return answerOptionRepository.save(answerOption);
            }).toList();
        }

        return AnswerResponse.from(answer, answerOptions);
    }

    @Override
    public AttemptResponse completeQuizAttempt(UUID attemptUuid) {
        var attempt = findQuizAttemptEntityByUuid(attemptUuid);

        if (attempt.getStatus() != AttemptStatus.IN_PROGRESS) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Attempt is not in progress");
        }

        var answers = answerRepository.findByAttemptId(attempt.getId());

        BigDecimal totalScore = answers.stream()
                .map(QuizAnswer::getMarksAwarded)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMarks = attempt.getQuiz().getTotalMarks();
        BigDecimal scorePct = BigDecimal.ZERO;
        if (totalMarks.compareTo(BigDecimal.ZERO) > 0) {
            scorePct = totalScore.multiply(new BigDecimal("100"))
                    .divide(totalMarks, 2, RoundingMode.HALF_UP);
        }

        boolean passed = scorePct.compareTo(attempt.getQuiz().getPassMarkPct()) >= 0;

        attempt.setScore(totalScore);
        attempt.setScorePct(scorePct);
        attempt.setPassed(passed);
        attempt.setCompletedAt(Instant.now());
        attempt.setStatus(AttemptStatus.COMPLETED);

        return AttemptResponse.from(attemptRepository.save(attempt));
    }

    @Override
    @Transactional(readOnly = true)
    public AttemptResponse findQuizAttemptByUuid(UUID attemptUuid) {
        return AttemptResponse.from(findQuizAttemptEntityByUuid(attemptUuid));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnswerResponse> findQuizAttemptAnswers(UUID attemptUuid) {
        var attempt = findQuizAttemptEntityByUuid(attemptUuid);
        var answers = answerRepository.findByAttemptId(attempt.getId());
        return answers.stream().map(answer -> {
            var answerOptions = answerOptionRepository.findByAnswerId(answer.getId());
            return AnswerResponse.from(answer, answerOptions);
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttemptResponse> findQuizAttemptsByQuizAndStudent(UUID quizUuid, UUID studentUuid) {
        var quiz = quizService.findQuizEntityByUuid(quizUuid);
        var student = findStudent(studentUuid);
        return attemptRepository.findByQuizIdAndStudentId(quiz.getId(), student.getId())
                .stream().map(AttemptResponse::from).toList();
    }

    @Override
    public QuizAttempt findQuizAttemptEntityByUuid(UUID uuid) {
        return attemptRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Quiz attempt was not found"));
    }

    private Student findStudent(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }

    private QuizQuestionOption findOption(UUID uuid) {
        return questionOptionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Question option was not found"));
    }
}
