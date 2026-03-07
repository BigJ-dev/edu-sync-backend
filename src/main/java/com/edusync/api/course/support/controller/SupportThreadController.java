package com.edusync.api.course.support.controller;

import com.edusync.api.course.support.controller.api.SupportThreadApi;
import com.edusync.api.course.support.dto.SupportThreadRequest;
import com.edusync.api.course.support.dto.SupportThreadResponse;
import com.edusync.api.course.support.enums.ThreadPriority;
import com.edusync.api.course.support.enums.ThreadStatus;
import com.edusync.api.course.support.service.SupportThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class SupportThreadController implements SupportThreadApi {

    private final SupportThreadService service;

    @Override
    public SupportThreadResponse create(UUID courseUuid, SupportThreadRequest.Create request) {
        return service.createThread(request);
    }

    @Override
    public List<SupportThreadResponse> findAll(UUID courseUuid, ThreadStatus status, ThreadPriority priority, Boolean escalated, String search) {
        return service.findAllThreads(courseUuid, status, priority, escalated, search);
    }

    @Override
    public SupportThreadResponse findByUuid(UUID courseUuid, UUID threadUuid) {
        return service.findThreadByUuid(threadUuid);
    }

    @Override
    public SupportThreadResponse updateStatus(UUID courseUuid, UUID threadUuid, SupportThreadRequest.UpdateStatus request) {
        return service.updateThreadStatus(threadUuid, request);
    }

    @Override
    public SupportThreadResponse escalate(UUID courseUuid, UUID threadUuid, UUID escalatedByUuid) {
        return service.escalateThread(threadUuid, escalatedByUuid);
    }
}
