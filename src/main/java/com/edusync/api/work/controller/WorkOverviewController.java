package com.edusync.api.work.controller;

import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.work.controller.api.WorkOverviewApi;
import com.edusync.api.work.dto.LecturerWorkResponse;
import com.edusync.api.work.dto.StudentWorkResponse;
import com.edusync.api.work.service.WorkOverviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class WorkOverviewController implements WorkOverviewApi {

    private final WorkOverviewService service;

    @Override
    public LecturerWorkResponse getLecturerWork(UUID lecturerUuid, AssessmentStatus assessmentStatus,
                                                 QuizStatus quizStatus, String search) {
        return service.getLecturerWork(lecturerUuid, assessmentStatus, quizStatus, search);
    }

    @Override
    public StudentWorkResponse getStudentWork(UUID studentUuid) {
        return service.getStudentWork(studentUuid);
    }
}
