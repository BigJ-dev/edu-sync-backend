package com.edusync.api.work.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.assessment.common.dto.AssessmentResponse;
import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.assessment.common.model.Assessment;
import com.edusync.api.course.assessment.common.repo.AssessmentRepository;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import com.edusync.api.course.assessment.submission.repo.SubmissionRepository;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import com.edusync.api.course.quiz.attempt.model.QuizAttempt;
import com.edusync.api.course.quiz.attempt.repo.AttemptRepository;
import com.edusync.api.course.quiz.common.dto.QuizResponse;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.course.quiz.common.model.Quiz;
import com.edusync.api.course.quiz.common.repo.QuizRepository;
import com.edusync.api.work.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkOverviewServiceImpl implements WorkOverviewService {

    private final AppUserRepository appUserRepository;
    private final StudentRepository studentRepository;
    private final AssessmentRepository assessmentRepository;
    private final QuizRepository quizRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SubmissionRepository submissionRepository;
    private final AttemptRepository attemptRepository;

    @Override
    public LecturerWorkResponse getLecturerWork(UUID lecturerUuid, AssessmentStatus assessmentStatus,
                                                 QuizStatus quizStatus, String search) {
        var lecturer = findAppUser(lecturerUuid);

        var assessments = assessmentRepository.findByCreatedById(lecturer.getId()).stream()
                .filter(a -> assessmentStatus == null || a.getStatus() == assessmentStatus)
                .filter(a -> search == null || search.isBlank() || a.getTitle().toLowerCase().contains(search.toLowerCase()))
                .map(AssessmentResponse::from)
                .toList();

        var quizzes = quizRepository.findByCreatedById(lecturer.getId()).stream()
                .filter(q -> quizStatus == null || q.getStatus() == quizStatus)
                .filter(q -> search == null || search.isBlank() || q.getTitle().toLowerCase().contains(search.toLowerCase()))
                .map(QuizResponse::from)
                .toList();

        return new LecturerWorkResponse(lecturerUuid, assessments, quizzes, assessments.size(), quizzes.size());
    }

    @Override
    public StudentWorkResponse getStudentWork(UUID studentUuid) {
        var student = findStudent(studentUuid);

        var enrollments = enrollmentRepository.findByStudentIdAndStatus(student.getId(), EnrollmentStatus.ENROLLED);
        var courseIds = enrollments.stream().map(e -> e.getCourse().getId()).toList();

        if (courseIds.isEmpty()) {
            return new StudentWorkResponse(studentUuid, List.of(), List.of(), 0, 0);
        }

        // Get all published assessments and quizzes across enrolled courses
        var allAssessments = courseIds.stream()
                .flatMap(courseId -> assessmentRepository.findByModuleCourseId(courseId).stream())
                .filter(a -> a.getStatus() == AssessmentStatus.PUBLISHED || a.getStatus() == AssessmentStatus.CLOSED || a.getStatus() == AssessmentStatus.GRADED)
                .toList();

        var allQuizzes = courseIds.stream()
                .flatMap(courseId -> quizRepository.findByModuleCourseId(courseId).stream())
                .filter(q -> q.getStatus() == QuizStatus.PUBLISHED || q.getStatus() == QuizStatus.CLOSED)
                .toList();

        // Get student submissions and attempts
        var assessmentIds = allAssessments.stream().map(Assessment::getId).toList();
        var quizIds = allQuizzes.stream().map(Quiz::getId).toList();

        var submissions = assessmentIds.isEmpty()
                ? List.<AssessmentSubmission>of()
                : submissionRepository.findByAssessmentIdInAndStudentId(assessmentIds, student.getId());
        var submissionByAssessment = submissions.stream()
                .collect(Collectors.toMap(s -> s.getAssessment().getId(), s -> s, (a, b) -> a));

        var attempts = quizIds.isEmpty()
                ? List.<QuizAttempt>of()
                : attemptRepository.findByQuizIdInAndStudentId(quizIds, student.getId());
        var attemptsByQuiz = attempts.stream()
                .collect(Collectors.groupingBy(a -> a.getQuiz().getId()));

        // Build assessment items
        var assessmentItems = allAssessments.stream()
                .map(assessment -> {
                    var submission = submissionByAssessment.get(assessment.getId());
                    var course = assessment.getModule().getCourse();
                    return new StudentAssessmentItem(
                            assessment.getUuid(),
                            course.getCode(),
                            course.getTitle(),
                            assessment.getModule().getTitle(),
                            assessment.getTitle(),
                            assessment.getAssessmentType(),
                            assessment.getTotalMarks(),
                            assessment.getDueDate(),
                            assessment.getStatus(),
                            submission != null,
                            submission != null ? submission.getStatus() : null,
                            submission != null ? submission.getMarksObtained() : null,
                            submission != null ? submission.getSubmittedAt() : null
                    );
                })
                .toList();

        // Build quiz items
        var quizItems = allQuizzes.stream()
                .map(quiz -> {
                    var quizAttempts = attemptsByQuiz.getOrDefault(quiz.getId(), List.of());
                    var course = quiz.getModule().getCourse();

                    var bestAttempt = quizAttempts.stream()
                            .filter(a -> Objects.nonNull(a.getScore()))
                            .max(Comparator.comparing(QuizAttempt::getScore))
                            .orElse(null);

                    var latestAttempt = quizAttempts.stream()
                            .max(Comparator.comparing(QuizAttempt::getStartedAt))
                            .orElse(null);

                    return new StudentQuizItem(
                            quiz.getUuid(),
                            course.getCode(),
                            course.getTitle(),
                            quiz.getModule().getTitle(),
                            quiz.getTitle(),
                            quiz.getTotalMarks(),
                            quiz.getTimeLimitMinutes(),
                            quiz.getMaxAttempts(),
                            quiz.getVisibleFrom(),
                            quiz.getVisibleUntil(),
                            quiz.getStatus(),
                            quizAttempts.size(),
                            bestAttempt != null ? bestAttempt.getScore() : null,
                            bestAttempt != null ? bestAttempt.getScorePct() : null,
                            bestAttempt != null ? bestAttempt.getPassed() : null,
                            latestAttempt != null ? latestAttempt.getStatus() : null
                    );
                })
                .toList();

        return new StudentWorkResponse(studentUuid, assessmentItems, quizItems, assessmentItems.size(), quizItems.size());
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }

    private Student findStudent(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }
}
