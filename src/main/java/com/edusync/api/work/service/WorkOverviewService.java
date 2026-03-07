package com.edusync.api.work.service;

import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.work.dto.LecturerWorkResponse;
import com.edusync.api.work.dto.StudentWorkResponse;

import java.util.UUID;

public interface WorkOverviewService {

    LecturerWorkResponse getLecturerWork(UUID lecturerUuid, AssessmentStatus assessmentStatus,
                                          QuizStatus quizStatus, String search);

    StudentWorkResponse getStudentWork(UUID studentUuid);
}
