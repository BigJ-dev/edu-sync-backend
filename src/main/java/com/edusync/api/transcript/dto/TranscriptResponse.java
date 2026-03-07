package com.edusync.api.transcript.dto;

import java.util.List;
import java.util.UUID;

public record TranscriptResponse(
        UUID studentUuid,
        String studentNumber,
        String firstName,
        String lastName,
        String email,
        List<TranscriptCourseEntry> courses
) {}
