package com.edusync.api.transcript.controller;

import com.edusync.api.transcript.controller.api.TranscriptApi;
import com.edusync.api.transcript.dto.TranscriptResponse;
import com.edusync.api.transcript.service.TranscriptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TranscriptController implements TranscriptApi {

    private final TranscriptService service;

    @Override
    public TranscriptResponse getStudentTranscript(UUID studentUuid) {
        return service.getStudentTranscript(studentUuid);
    }
}
