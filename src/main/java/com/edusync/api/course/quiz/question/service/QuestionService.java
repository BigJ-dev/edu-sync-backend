package com.edusync.api.course.quiz.question.service;

import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.quiz.common.model.Quiz;
import com.edusync.api.course.quiz.common.service.QuizService;
import com.edusync.api.course.quiz.question.dto.OptionResponse;
import com.edusync.api.course.quiz.question.dto.QuestionRequest;
import com.edusync.api.course.quiz.question.dto.QuestionResponse;
import com.edusync.api.course.quiz.question.model.QuizQuestion;
import com.edusync.api.course.quiz.question.model.QuizQuestionOption;
import com.edusync.api.course.quiz.question.repo.QuestionOptionRepository;
import com.edusync.api.course.quiz.question.repo.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final QuizService quizService;

    public QuestionResponse addQuestion(UUID quizUuid, QuestionRequest.Create request) {
        var quiz = quizService.findQuiz(quizUuid);

        var question = QuizQuestion.builder()
                .quiz(quiz)
                .questionText(request.questionText())
                .questionType(request.questionType())
                .imageS3Key(request.imageS3Key())
                .marks(request.marks())
                .sortOrder(request.sortOrder())
                .explanation(request.explanation())
                .build();

        question = questionRepository.save(question);

        List<OptionResponse> optionResponses = List.of();
        if (request.options() != null && !request.options().isEmpty()) {
            optionResponses = createOptions(question, request.options());
        }

        return QuestionResponse.from(question, optionResponses);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> findByQuiz(UUID quizUuid) {
        var quiz = quizService.findQuiz(quizUuid);
        var questions = questionRepository.findByQuizIdOrderBySortOrderAsc(quiz.getId());
        return questions.stream().map(q -> {
            var options = optionRepository.findByQuestionIdOrderBySortOrderAsc(q.getId())
                    .stream().map(OptionResponse::from).toList();
            return QuestionResponse.from(q, options);
        }).toList();
    }

    @Transactional(readOnly = true)
    public QuestionResponse findByUuid(UUID questionUuid) {
        var question = findQuestion(questionUuid);
        var options = optionRepository.findByQuestionIdOrderBySortOrderAsc(question.getId())
                .stream().map(OptionResponse::from).toList();
        return QuestionResponse.from(question, options);
    }

    public QuestionResponse updateQuestion(UUID questionUuid, QuestionRequest.Update request) {
        var question = findQuestion(questionUuid);
        question.setQuestionText(request.questionText());
        question.setImageS3Key(request.imageS3Key());
        question.setMarks(request.marks());
        question.setSortOrder(request.sortOrder());
        question.setExplanation(request.explanation());
        question = questionRepository.save(question);

        var options = optionRepository.findByQuestionIdOrderBySortOrderAsc(question.getId())
                .stream().map(OptionResponse::from).toList();
        return QuestionResponse.from(question, options);
    }

    public void deleteQuestion(UUID questionUuid) {
        var question = findQuestion(questionUuid);
        optionRepository.deleteByQuestionId(question.getId());
        questionRepository.delete(question);
    }

    public List<OptionResponse> replaceOptions(UUID questionUuid, List<QuestionRequest.OptionCreate> options) {
        var question = findQuestion(questionUuid);
        optionRepository.deleteByQuestionId(question.getId());
        return createOptions(question, options);
    }

    public QuizQuestion findQuestion(UUID uuid) {
        return questionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Quiz question was not found"));
    }

    private List<OptionResponse> createOptions(QuizQuestion question, List<QuestionRequest.OptionCreate> options) {
        return options.stream().map(opt -> {
            var option = QuizQuestionOption.builder()
                    .question(question)
                    .optionText(opt.optionText())
                    .isCorrect(opt.isCorrect())
                    .feedback(opt.feedback())
                    .sortOrder(opt.sortOrder())
                    .build();
            return OptionResponse.from(optionRepository.save(option));
        }).toList();
    }
}
