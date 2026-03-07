package com.edusync.api.course.grading.service;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.assessment.common.enums.AssessmentType;
import com.edusync.api.course.assessment.common.model.Assessment;
import com.edusync.api.course.assessment.common.repo.AssessmentRepository;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import com.edusync.api.course.assessment.submission.repo.SubmissionRepository;
import com.edusync.api.course.common.model.Course;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.grading.dto.*;
import com.edusync.api.course.grading.enums.GradeCategoryType;
import com.edusync.api.course.grading.model.GradeWeightCategory;
import com.edusync.api.course.grading.repo.GradeWeightCategoryRepository;
import com.edusync.api.course.quiz.attempt.model.QuizAttempt;
import com.edusync.api.course.quiz.attempt.repo.AttemptRepository;
import com.edusync.api.course.quiz.common.model.Quiz;
import com.edusync.api.course.quiz.common.repo.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GradingServiceImpl implements GradingService {

    private final GradeWeightCategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final AssessmentRepository assessmentRepository;
    private final SubmissionRepository submissionRepository;
    private final QuizRepository quizRepository;
    private final AttemptRepository attemptRepository;

    @Override
    public GradeWeightResponse createCategory(UUID courseUuid, GradeWeightRequest.Create request) {
        var course = findCourse(courseUuid);

        var category = GradeWeightCategory.builder()
                .course(course)
                .name(request.name())
                .categoryType(request.categoryType())
                .weightPct(request.weightPct())
                .sortOrder(request.sortOrder())
                .build();

        return GradeWeightResponse.from(categoryRepository.save(category));
    }

    @Override
    public GradeWeightResponse updateCategory(UUID categoryUuid, GradeWeightRequest.Update request) {
        var category = findCategory(categoryUuid);

        if (Objects.nonNull(request.name())) category.setName(request.name());
        if (Objects.nonNull(request.weightPct())) category.setWeightPct(request.weightPct());
        if (Objects.nonNull(request.sortOrder())) category.setSortOrder(request.sortOrder());

        return GradeWeightResponse.from(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(UUID categoryUuid) {
        categoryRepository.delete(findCategory(categoryUuid));
    }

    @Override
    @Transactional(readOnly = true)
    public List<GradeWeightResponse> findCategoriesByCourse(UUID courseUuid) {
        var course = findCourse(courseUuid);
        return categoryRepository.findByCourseIdOrderBySortOrder(course.getId())
                .stream().map(GradeWeightResponse::from).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FinalGradeResponse calculateFinalGrade(UUID courseUuid, UUID studentUuid) {
        var course = findCourse(courseUuid);
        var student = findStudent(studentUuid);
        var categories = categoryRepository.findByCourseIdOrderBySortOrder(course.getId());

        if (categories.isEmpty()) {
            throw new ServiceException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "No grade weight categories configured for this course");
        }

        var assessments = assessmentRepository.findByModuleCourseId(course.getId());
        var quizzes = quizRepository.findByModuleCourseId(course.getId());

        var assessmentIds = assessments.stream().map(Assessment::getId).toList();
        var quizIds = quizzes.stream().map(Quiz::getId).toList();

        var submissions = assessmentIds.isEmpty()
                ? List.<AssessmentSubmission>of()
                : submissionRepository.findByAssessmentIdInAndStudentId(assessmentIds, student.getId());
        var attempts = quizIds.isEmpty()
                ? List.<QuizAttempt>of()
                : attemptRepository.findByQuizIdInAndStudentId(quizIds, student.getId());

        // Index submissions by assessment ID
        var submissionByAssessment = submissions.stream()
                .collect(Collectors.toMap(s -> s.getAssessment().getId(), s -> s, (a, b) -> a));

        // Index best quiz attempt by quiz ID (highest score)
        var bestAttemptByQuiz = attempts.stream()
                .filter(a -> Objects.nonNull(a.getScore()))
                .collect(Collectors.toMap(
                        a -> a.getQuiz().getId(),
                        a -> a,
                        (a, b) -> a.getScore().compareTo(b.getScore()) >= 0 ? a : b));

        var categoryDetails = new ArrayList<CategoryGradeDetail>();
        var finalGrade = BigDecimal.ZERO;
        var totalWeight = BigDecimal.ZERO;

        for (var category : categories) {
            totalWeight = totalWeight.add(category.getWeightPct());

            var detail = switch (category.getCategoryType()) {
                case QUIZ -> calculateQuizCategory(category, quizzes, bestAttemptByQuiz);
                default -> calculateAssessmentCategory(category, assessments, submissionByAssessment);
            };

            categoryDetails.add(detail);
            finalGrade = finalGrade.add(detail.weightedContribution());
        }

        return new FinalGradeResponse(
                courseUuid,
                studentUuid,
                finalGrade.setScale(2, RoundingMode.HALF_UP),
                totalWeight,
                categoryDetails
        );
    }

    private CategoryGradeDetail calculateAssessmentCategory(
            GradeWeightCategory category,
            List<Assessment> allAssessments,
            Map<Long, AssessmentSubmission> submissionByAssessment) {

        var targetType = mapCategoryToAssessmentType(category.getCategoryType());

        var matching = allAssessments.stream()
                .filter(a -> a.getAssessmentType() == targetType)
                .toList();

        int totalItems = matching.size();
        var gradedMarks = new ArrayList<BigDecimal>();

        for (var assessment : matching) {
            var submission = submissionByAssessment.get(assessment.getId());
            if (submission != null && submission.getMarksObtained() != null) {
                var pct = submission.getMarksObtained()
                        .multiply(BigDecimal.valueOf(100))
                        .divide(assessment.getTotalMarks(), 4, RoundingMode.HALF_UP);
                gradedMarks.add(pct);
            }
        }

        var avgPct = gradedMarks.isEmpty()
                ? BigDecimal.ZERO
                : gradedMarks.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(gradedMarks.size()), 4, RoundingMode.HALF_UP);

        var weighted = avgPct.multiply(category.getWeightPct())
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        return new CategoryGradeDetail(
                category.getName(),
                category.getCategoryType(),
                category.getWeightPct(),
                avgPct.setScale(2, RoundingMode.HALF_UP),
                weighted.setScale(2, RoundingMode.HALF_UP),
                gradedMarks.size(),
                totalItems
        );
    }

    private CategoryGradeDetail calculateQuizCategory(
            GradeWeightCategory category,
            List<Quiz> allQuizzes,
            Map<Long, QuizAttempt> bestAttemptByQuiz) {

        int totalItems = allQuizzes.size();
        var gradedPcts = new ArrayList<BigDecimal>();

        for (var quiz : allQuizzes) {
            var attempt = bestAttemptByQuiz.get(quiz.getId());
            if (attempt != null && attempt.getScore() != null) {
                var pct = attempt.getScore()
                        .multiply(BigDecimal.valueOf(100))
                        .divide(quiz.getTotalMarks(), 4, RoundingMode.HALF_UP);
                gradedPcts.add(pct);
            }
        }

        var avgPct = gradedPcts.isEmpty()
                ? BigDecimal.ZERO
                : gradedPcts.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(gradedPcts.size()), 4, RoundingMode.HALF_UP);

        var weighted = avgPct.multiply(category.getWeightPct())
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        return new CategoryGradeDetail(
                category.getName(),
                category.getCategoryType(),
                category.getWeightPct(),
                avgPct.setScale(2, RoundingMode.HALF_UP),
                weighted.setScale(2, RoundingMode.HALF_UP),
                gradedPcts.size(),
                totalItems
        );
    }

    private AssessmentType mapCategoryToAssessmentType(GradeCategoryType categoryType) {
        return switch (categoryType) {
            case ASSIGNMENT -> AssessmentType.ASSIGNMENT;
            case PROJECT -> AssessmentType.PROJECT;
            case PRACTICAL -> AssessmentType.PRACTICAL;
            case PRESENTATION -> AssessmentType.PRESENTATION;
            case EXAM, OTHER -> AssessmentType.OTHER;
            case QUIZ -> throw new IllegalArgumentException("QUIZ category should use quiz calculation");
        };
    }

    private Course findCourse(UUID uuid) {
        return courseRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course was not found"));
    }

    private Student findStudent(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }

    private GradeWeightCategory findCategory(UUID uuid) {
        return categoryRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Grade weight category was not found"));
    }
}
