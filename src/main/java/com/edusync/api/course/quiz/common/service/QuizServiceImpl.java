package com.edusync.api.course.quiz.common.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.module.model.CourseModule;
import com.edusync.api.course.module.repo.ModuleRepository;
import com.edusync.api.course.quiz.common.dto.QuizRequest;
import com.edusync.api.course.quiz.common.dto.QuizResponse;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.course.quiz.common.model.Quiz;
import com.edusync.api.course.quiz.common.repo.QuizRepository;
import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.course.session.repo.ClassSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Transactional
public class QuizServiceImpl implements QuizService {

    private static final BigDecimal DEFAULT_PASS_MARK_PCT = new BigDecimal("50");
    private static final int DEFAULT_MAX_ATTEMPTS = 1;

    private final QuizRepository repository;
    private final ModuleRepository moduleRepository;
    private final AppUserRepository appUserRepository;
    private final ClassSessionRepository classSessionRepository;

    @Override
    public QuizResponse createQuiz(UUID moduleUuid, QuizRequest.Create request) {
        var module = findModule(moduleUuid);
        var createdBy = findAppUser(request.createdByUuid());
        var classSession = Optional.ofNullable(request.classSessionUuid())
                .map(this::findClassSession)
                .orElse(null);

        var quiz = Quiz.builder()
                .module(module)
                .classSession(classSession)
                .createdBy(createdBy)
                .title(request.title())
                .description(request.description())
                .timeLimitMinutes(request.timeLimitMinutes())
                .totalMarks(request.totalMarks())
                .passMarkPct(Objects.requireNonNullElse(request.passMarkPct(), DEFAULT_PASS_MARK_PCT))
                .weightPct(request.weightPct())
                .maxAttempts(Objects.requireNonNullElse(request.maxAttempts(), DEFAULT_MAX_ATTEMPTS))
                .shuffleQuestions(Objects.requireNonNullElse(request.shuffleQuestions(), false))
                .showAnswersAfter(Objects.requireNonNullElse(request.showAnswersAfter(), true))
                .documentS3Key(request.documentS3Key())
                .documentName(request.documentName())
                .visibleFrom(request.visibleFrom())
                .visibleUntil(request.visibleUntil())
                .status(QuizStatus.DRAFT)
                .build();

        return QuizResponse.from(repository.save(quiz));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizResponse> findAllQuizzesByModule(UUID moduleUuid, QuizStatus status, String search) {
        var module = findModule(moduleUuid);

        var spec = Specification.where(QuizSpec.hasModuleId(module.getId()))
                .and(QuizSpec.hasStatus(status))
                .and(QuizSpec.searchByTitle(search));

        return repository.findAll(spec).stream()
                .map(QuizResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public QuizResponse findQuizByUuid(UUID quizUuid) {
        return QuizResponse.from(findQuizEntityByUuid(quizUuid));
    }

    @Override
    public QuizResponse updateQuiz(UUID quizUuid, QuizRequest.Update request) {
        var quiz = findQuizEntityByUuid(quizUuid);

        quiz.setTitle(request.title());
        quiz.setDescription(request.description());
        quiz.setTimeLimitMinutes(request.timeLimitMinutes());
        quiz.setTotalMarks(request.totalMarks());
        quiz.setWeightPct(request.weightPct());
        quiz.setDocumentS3Key(request.documentS3Key());
        quiz.setDocumentName(request.documentName());
        quiz.setVisibleFrom(request.visibleFrom());
        quiz.setVisibleUntil(request.visibleUntil());

        Optional.ofNullable(request.passMarkPct()).ifPresent(quiz::setPassMarkPct);
        Optional.ofNullable(request.maxAttempts()).ifPresent(quiz::setMaxAttempts);
        Optional.ofNullable(request.shuffleQuestions()).ifPresent(quiz::setShuffleQuestions);
        Optional.ofNullable(request.showAnswersAfter()).ifPresent(quiz::setShowAnswersAfter);

        return QuizResponse.from(repository.save(quiz));
    }

    @Override
    public QuizResponse updateQuizStatus(UUID quizUuid, QuizRequest.UpdateStatus request) {
        var quiz = findQuizEntityByUuid(quizUuid);
        quiz.setStatus(request.status());
        return QuizResponse.from(repository.save(quiz));
    }

    @Override
    public void deleteQuiz(UUID quizUuid) {
        repository.delete(findQuizEntityByUuid(quizUuid));
    }

    @Override
    public QuizResponse duplicateQuiz(UUID quizUuid, UUID targetModuleUuid) {
        var source = findQuizEntityByUuid(quizUuid);
        var targetModule = findModule(targetModuleUuid);

        var copy = Quiz.builder()
                .module(targetModule)
                .createdBy(source.getCreatedBy())
                .title(source.getTitle() + " (Copy)")
                .description(source.getDescription())
                .timeLimitMinutes(source.getTimeLimitMinutes())
                .totalMarks(source.getTotalMarks())
                .passMarkPct(source.getPassMarkPct())
                .weightPct(source.getWeightPct())
                .maxAttempts(source.getMaxAttempts())
                .shuffleQuestions(source.isShuffleQuestions())
                .showAnswersAfter(source.isShowAnswersAfter())
                .documentS3Key(source.getDocumentS3Key())
                .documentName(source.getDocumentName())
                .visibleFrom(source.getVisibleFrom())
                .visibleUntil(source.getVisibleUntil())
                .status(QuizStatus.DRAFT)
                .build();

        return QuizResponse.from(repository.save(copy));
    }

    @Override
    public QuizResponse reopenQuiz(UUID quizUuid, QuizRequest.Reopen request) {
        var quiz = findQuizEntityByUuid(quizUuid);

        quiz.setStatus(QuizStatus.PUBLISHED);
        quiz.setVisibleFrom(request.visibleFrom());
        quiz.setVisibleUntil(request.visibleUntil());

        Optional.ofNullable(request.additionalAttempts())
                .ifPresent(extra -> quiz.setMaxAttempts(quiz.getMaxAttempts() + extra));

        return QuizResponse.from(repository.save(quiz));
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizResponse> findAllByLecturer(UUID lecturerUuid, QuizStatus status, String search) {
        var lecturer = findAppUser(lecturerUuid);

        Predicate<Quiz> statusFilter = q -> Objects.isNull(status) || q.getStatus() == status;
        Predicate<Quiz> searchFilter = q -> Objects.isNull(search) || search.isBlank()
                || q.getTitle().toLowerCase().contains(search.toLowerCase());

        return repository.findByCreatedById(lecturer.getId()).stream()
                .filter(statusFilter.and(searchFilter))
                .map(QuizResponse::from)
                .toList();
    }

    @Override
    public Quiz findQuizEntityByUuid(UUID uuid) {
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
