package com.edusync.api.work.controller.api;

import com.edusync.api.course.assessment.common.enums.AssessmentStatus;
import com.edusync.api.course.quiz.common.enums.QuizStatus;
import com.edusync.api.work.dto.LecturerWorkResponse;
import com.edusync.api.work.dto.StudentWorkResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "Work Overview", description = "Cross-course work overview for lecturers and students. " +
        "Lecturers can view all assessments and quizzes they created across all courses. " +
        "Students can view all assigned work across enrolled courses with submission/attempt status.")
@RequestMapping("/work")
public interface WorkOverviewApi {

    @GetMapping("/lecturers/{lecturerUuid}")
    @Operation(
            summary = "Get lecturer work overview",
            description = "Returns all assessments and quizzes created by a lecturer across all courses. " +
                    "Supports filtering by assessment status, quiz status, and searching by title."
    )
    @ApiResponse(responseCode = "200", description = "Lecturer work overview retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Lecturer not found")
    LecturerWorkResponse getLecturerWork(
            @PathVariable UUID lecturerUuid,
            @RequestParam(required = false) AssessmentStatus assessmentStatus,
            @RequestParam(required = false) QuizStatus quizStatus,
            @RequestParam(required = false) String search);

    @GetMapping("/students/{studentUuid}")
    @Operation(
            summary = "Get student work overview",
            description = "Returns all assigned assessments and quizzes for a student across all enrolled courses. " +
                    "Includes submission status, marks obtained, quiz attempts taken, and best scores."
    )
    @ApiResponse(responseCode = "200", description = "Student work overview retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    StudentWorkResponse getStudentWork(@PathVariable UUID studentUuid);
}
