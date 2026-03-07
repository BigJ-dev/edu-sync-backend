package com.edusync.api.transcript.controller.api;

import com.edusync.api.transcript.dto.TranscriptResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Transcripts", description = "Endpoints for viewing student academic transcripts including grades, attendance, and assessment results across all enrolled courses.")
@RequestMapping("/transcripts")
public interface TranscriptApi {

    @GetMapping("/student/{studentUuid}")
    @Operation(
            summary = "Get student transcript",
            description = "Returns a comprehensive academic transcript for a student, including all enrolled courses, " +
                    "assessment grades, attendance percentages, and overall averages."
    )
    @ApiResponse(responseCode = "200", description = "Transcript retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Student not found")
    TranscriptResponse getStudentTranscript(@PathVariable UUID studentUuid);
}
