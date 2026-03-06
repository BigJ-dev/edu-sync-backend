package com.edusync.api.course.assessment.submission.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.assessment.common.model.Assessment;
import com.edusync.api.course.assessment.common.service.AssessmentService;
import com.edusync.api.course.assessment.submission.dto.SubmissionRequest;
import com.edusync.api.course.assessment.submission.dto.SubmissionResponse;
import com.edusync.api.course.assessment.submission.enums.SubmissionStatus;
import com.edusync.api.course.assessment.submission.model.AssessmentSubmission;
import com.edusync.api.course.assessment.submission.repo.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionService {

    private final SubmissionRepository repository;
    private final AssessmentService assessmentService;
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;

    public SubmissionResponse submit(UUID assessmentUuid, SubmissionRequest.Submit request) {
        var assessment = assessmentService.findAssessment(assessmentUuid);
        var student = findStudent(request.studentUuid());

        if (repository.existsByAssessmentIdAndStudentId(assessment.getId(), student.getId()))
            throw new ServiceException(HttpStatus.CONFLICT, "Student has already submitted for this assessment");

        var now = Instant.now();
        var isLate = now.isAfter(assessment.getDueDate());

        if (isLate && !assessment.isAllowLateSubmission())
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Late submissions are not allowed for this assessment");

        var submission = AssessmentSubmission.builder()
                .assessment(assessment)
                .student(student)
                .submissionText(request.submissionText())
                .s3Key(request.s3Key())
                .fileName(request.fileName())
                .fileSizeBytes(request.fileSizeBytes())
                .mimeType(request.mimeType())
                .submittedAt(now)
                .isLate(isLate)
                .status(SubmissionStatus.SUBMITTED)
                .build();

        return SubmissionResponse.from(repository.save(submission));
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> findAllByAssessment(UUID assessmentUuid, SubmissionStatus status, Boolean late) {
        var assessment = assessmentService.findAssessment(assessmentUuid);
        var spec = Specification.where(SubmissionSpec.hasAssessmentId(assessment.getId()))
                .and(SubmissionSpec.hasStatus(status))
                .and(SubmissionSpec.isLate(late));
        return repository.findAll(spec).stream().map(SubmissionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SubmissionResponse findByUuid(UUID submissionUuid) {
        return SubmissionResponse.from(findSubmission(submissionUuid));
    }

    public SubmissionResponse grade(UUID submissionUuid, SubmissionRequest.Grade request) {
        var submission = findSubmission(submissionUuid);
        var gradedBy = findAppUser(request.gradedByUuid());

        submission.setMarksObtained(request.marksObtained());
        submission.setFeedback(request.feedback());
        submission.setGradedBy(gradedBy);
        submission.setGradedAt(Instant.now());
        submission.setStatus(SubmissionStatus.GRADED);
        return SubmissionResponse.from(repository.save(submission));
    }

    public SubmissionResponse returnForResubmission(UUID submissionUuid, SubmissionRequest.Return request) {
        var submission = findSubmission(submissionUuid);
        submission.setFeedback(request.feedback());
        submission.setStatus(SubmissionStatus.RETURNED);
        return SubmissionResponse.from(repository.save(submission));
    }

    public AssessmentSubmission findSubmission(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Submission was not found"));
    }

    private Student findStudent(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }
}
