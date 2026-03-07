package com.edusync.api.transcript.service;

import com.edusync.api.transcript.dto.TranscriptResponse;

import java.util.UUID;

public interface TranscriptService {

    TranscriptResponse getStudentTranscript(UUID studentUuid);
}
