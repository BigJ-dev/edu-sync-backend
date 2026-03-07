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
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final QuizService quizService;

    @Override
    public QuestionResponse addQuizQuestion(UUID quizUuid, QuestionRequest.Create request) {
        var quiz = quizService.findQuizEntityByUuid(quizUuid);

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

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> findAllQuestionsByQuiz(UUID quizUuid) {
        var quiz = quizService.findQuizEntityByUuid(quizUuid);
        var questions = questionRepository.findByQuizIdOrderBySortOrderAsc(quiz.getId());
        return questions.stream().map(q -> {
            var options = optionRepository.findByQuestionIdOrderBySortOrderAsc(q.getId())
                    .stream().map(OptionResponse::from).toList();
            return QuestionResponse.from(q, options);
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse findQuizQuestionByUuid(UUID questionUuid) {
        var question = findQuizQuestionEntityByUuid(questionUuid);
        var options = optionRepository.findByQuestionIdOrderBySortOrderAsc(question.getId())
                .stream().map(OptionResponse::from).toList();
        return QuestionResponse.from(question, options);
    }

    @Override
    public QuestionResponse updateQuizQuestion(UUID questionUuid, QuestionRequest.Update request) {
        var question = findQuizQuestionEntityByUuid(questionUuid);
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

    @Override
    public void deleteQuizQuestion(UUID questionUuid) {
        var question = findQuizQuestionEntityByUuid(questionUuid);
        optionRepository.deleteByQuestionId(question.getId());
        questionRepository.delete(question);
    }

    @Override
    public List<OptionResponse> replaceQuizQuestionOptions(UUID questionUuid, List<QuestionRequest.OptionCreate> options) {
        var question = findQuizQuestionEntityByUuid(questionUuid);
        optionRepository.deleteByQuestionId(question.getId());
        return createOptions(question, options);
    }

    @Override
    public QuizQuestion findQuizQuestionEntityByUuid(UUID uuid) {
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
