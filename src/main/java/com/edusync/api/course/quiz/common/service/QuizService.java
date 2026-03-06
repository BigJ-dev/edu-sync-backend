package com.edusync.api.course.quiz.common.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.course.session.repo.ClassSessionRepository;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.module.repo.ModuleRepository;
import com.edusync.api.course.quiz.common.dto.QuizRequest;
import com.edusync.api.course.quiz.common.dto.QuizResponse;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.course.quiz.common.model.Quiz;
import com.edusync.api.course.quiz.common.repo.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizService {

    private final QuizRepository repository;
    private final ModuleRepository moduleRepository;
    private final AppUserRepository appUserRepository;
    private final ClassSessionRepository classSessionRepository;

    public QuizResponse create(UUID moduleUuid, QuizRequest.Create request) {
        var module = findModule(moduleUuid);
        var createdBy = findAppUser(request.createdByUuid());
        ClassSession classSession = null;
        if (request.classSessionUuid() != null) {
            classSession = findClassSession(request.classSessionUuid());
        }

        var quiz = Quiz.builder()
                .module(module)
                .classSession(classSession)
                .createdBy(createdBy)
                .title(request.title())
                .description(request.description())
                .timeLimitMinutes(request.timeLimitMinutes())
                .totalMarks(request.totalMarks())
                .passMarkPct(request.passMarkPct() != null ? request.passMarkPct() : new java.math.BigDecimal("50"))
                .weightPct(request.weightPct())
                .maxAttempts(request.maxAttempts() != null ? request.maxAttempts() : 1)
                .shuffleQuestions(request.shuffleQuestions() != null ? request.shuffleQuestions() : false)
                .showAnswersAfter(request.showAnswersAfter() != null ? request.showAnswersAfter() : true)
                .documentS3Key(request.documentS3Key())
                .documentName(request.documentName())
                .visibleFrom(request.visibleFrom())
                .visibleUntil(request.visibleUntil())
                .status(QuizStatus.DRAFT)
                .build();

        return QuizResponse.from(repository.save(quiz));
    }

    @Transactional(readOnly = true)
    public List<QuizResponse> findAllByModule(UUID moduleUuid, QuizStatus status, String search) {
        var module = findModule(moduleUuid);
        var spec = Specification.where(QuizSpec.hasModuleId(module.getId()))
                .and(QuizSpec.hasStatus(status))
                .and(QuizSpec.searchByTitle(search));
        return repository.findAll(spec).stream().map(QuizResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public QuizResponse findByUuid(UUID quizUuid) {
        return QuizResponse.from(findQuiz(quizUuid));
    }

    public QuizResponse update(UUID quizUuid, QuizRequest.Update request) {
        var quiz = findQuiz(quizUuid);
        quiz.setTitle(request.title());
        quiz.setDescription(request.description());
        quiz.setTimeLimitMinutes(request.timeLimitMinutes());
        quiz.setTotalMarks(request.totalMarks());
        if (request.passMarkPct() != null) quiz.setPassMarkPct(request.passMarkPct());
        quiz.setWeightPct(request.weightPct());
        if (request.maxAttempts() != null) quiz.setMaxAttempts(request.maxAttempts());
        if (request.shuffleQuestions() != null) quiz.setShuffleQuestions(request.shuffleQuestions());
        if (request.showAnswersAfter() != null) quiz.setShowAnswersAfter(request.showAnswersAfter());
        quiz.setDocumentS3Key(request.documentS3Key());
        quiz.setDocumentName(request.documentName());
        quiz.setVisibleFrom(request.visibleFrom());
        quiz.setVisibleUntil(request.visibleUntil());
        return QuizResponse.from(repository.save(quiz));
    }

    public QuizResponse updateStatus(UUID quizUuid, QuizRequest.UpdateStatus request) {
        var quiz = findQuiz(quizUuid);
        quiz.setStatus(request.status());
        return QuizResponse.from(repository.save(quiz));
    }

    public Quiz findQuiz(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Quiz was not found"));
    }

    private CourseModule findModule(UUID uuid) {
        return moduleRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Module was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }

    private ClassSession findClassSession(UUID uuid) {
        return classSessionRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Class session was not found"));
    }
}
