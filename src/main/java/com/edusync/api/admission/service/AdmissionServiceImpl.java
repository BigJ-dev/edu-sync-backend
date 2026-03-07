package com.edusync.api.admission.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.admission.dto.ApplicationRequest;
import com.edusync.api.admission.dto.ApplicationResponse;
import com.edusync.api.admission.enums.ApplicationStatus;
import com.edusync.api.admission.model.ApplicationCourseChoice;
import com.edusync.api.admission.model.ApplicationDocument;
import com.edusync.api.admission.model.StudentApplication;
import com.edusync.api.admission.repo.StudentApplicationRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.common.repo.CourseRepository;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.model.CourseEnrollment;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class AdmissionServiceImpl implements AdmissionService {

    private final StudentApplicationRepository applicationRepository;
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public ApplicationResponse submitApplication(ApplicationRequest.Submit request) {
        if (applicationRepository.existsByEmail(request.email())) {
            throw new ServiceException(HttpStatus.CONFLICT, "An application with this email already exists");
        }

        if (Objects.nonNull(request.idNumber()) && applicationRepository.existsByIdNumber(request.idNumber())) {
            throw new ServiceException(HttpStatus.CONFLICT, "An application with this ID number already exists");
        }

        if (studentRepository.existsByEmail(request.email())) {
            throw new ServiceException(HttpStatus.CONFLICT, "A student with this email is already registered");
        }

        var application = StudentApplication.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .idNumber(request.idNumber())
                .dateOfBirth(request.dateOfBirth())
                .address(request.address())
                .build();

        if (Objects.nonNull(request.documents()) && !request.documents().isEmpty()) {
            request.documents().stream()
                    .map(doc -> ApplicationDocument.builder()
                            .application(application)
                            .documentType(doc.documentType())
                            .documentName(doc.documentName())
                            .s3Key(doc.s3Key())
                            .fileSizeBytes(doc.fileSizeBytes())
                            .mimeType(doc.mimeType())
                            .build())
                    .forEach(application.getDocuments()::add);
        }

        var courseUuids = request.courseChoices();
        for (int i = 0; i < courseUuids.size(); i++) {
            var courseUuid = courseUuids.get(i);
            var course = courseRepository.findByUuid(courseUuid)
                    .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Course not found: " + courseUuid));
            application.getCourseChoices().add(
                    ApplicationCourseChoice.builder()
                            .application(application)
                            .course(course)
                            .priority(i + 1)
                            .build());
        }

        return ApplicationResponse.from(applicationRepository.save(application));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationResponse> findAllApplications(ApplicationRequest.Filter filter) {
        var spec = Stream.of(
                        ApplicationSpec.hasStatus(filter.status()),
                        ApplicationSpec.searchByNameOrEmail(filter.search()))
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());

        return applicationRepository.findAll(spec).stream()
                .map(ApplicationResponse::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationResponse findApplicationByUuid(UUID applicationUuid) {
        return ApplicationResponse.from(findApplication(applicationUuid));
    }

    @Override
    public ApplicationResponse reviewApplication(UUID applicationUuid, ApplicationRequest.Review request) {
        var application = findApplication(applicationUuid);

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Application has already been reviewed");
        }

        var reviewer = findAppUser(request.reviewerUuid());

        if (request.decision() == ApplicationStatus.APPROVED) {
            return approveApplication(application, reviewer);
        } else if (request.decision() == ApplicationStatus.REJECTED) {
            return rejectApplication(application, reviewer, request.rejectionReason());
        }

        throw new ServiceException(HttpStatus.BAD_REQUEST, "Decision must be APPROVED or REJECTED");
    }

    private ApplicationResponse approveApplication(StudentApplication application, AppUser reviewer) {
        var studentNumber = generateStudentNumber();

        var student = Student.builder()
                .studentNumber(studentNumber)
                .firstName(application.getFirstName())
                .lastName(application.getLastName())
                .email(application.getEmail())
                .phone(application.getPhone())
                .active(false)
                .build();

        var savedStudent = studentRepository.save(student);

        application.setStatus(ApplicationStatus.APPROVED);
        application.setReviewedBy(reviewer);
        application.setReviewedAt(Instant.now());
        application.setStudent(savedStudent);

        application.getCourseChoices().stream()
                .filter(choice -> choice.getPriority() == 1)
                .findFirst()
                .ifPresent(firstChoice -> enrollmentRepository.save(
                        CourseEnrollment.builder()
                                .course(firstChoice.getCourse())
                                .student(savedStudent)
                                .status(EnrollmentStatus.ENROLLED)
                                .enrolledAt(Instant.now())
                                .blocked(false)
                                .build()));

        return ApplicationResponse.from(applicationRepository.save(application));
    }

    private ApplicationResponse rejectApplication(StudentApplication application, AppUser reviewer, String reason) {
        if (Objects.isNull(reason) || reason.isBlank()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Rejection reason is required");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        application.setReviewedBy(reviewer);
        application.setReviewedAt(Instant.now());
        application.setRejectionReason(reason);

        return ApplicationResponse.from(applicationRepository.save(application));
    }

    private String generateStudentNumber() {
        var year = Year.now().getValue();
        var maxId = applicationRepository.findMaxStudentId();
        var sequence = maxId + 1;
        return "ES-%d-%04d".formatted(year, sequence);
    }

    private StudentApplication findApplication(UUID uuid) {
        return applicationRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Application was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }
}
