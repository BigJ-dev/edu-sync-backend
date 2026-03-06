package com.edusync.api.course.messaging.controller;

import com.edusync.api.course.messaging.controller.api.MessageThreadApi;
import com.edusync.api.course.messaging.dto.ThreadRequest;
import com.edusync.api.course.messaging.dto.ThreadResponse;
import com.edusync.api.course.messaging.enums.ThreadPriority;
import com.edusync.api.course.messaging.enums.ThreadStatus;
import com.edusync.api.course.messaging.service.MessageThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class MessageThreadController implements MessageThreadApi {

    private final MessageThreadService service;

    @Override
    public ThreadResponse create(UUID courseUuid, ThreadRequest.Create request) {
        return service.createThread(request);
    }

    @Override
    public List<ThreadResponse> findAll(UUID courseUuid, ThreadStatus status, ThreadPriority priority, Boolean escalated, String search) {
        return service.findAll(courseUuid, status, priority, escalated, search);
    }

    @Override
    public ThreadResponse findByUuid(UUID courseUuid, UUID threadUuid) {
        return service.findByUuid(threadUuid);
    }

    @Override
    public ThreadResponse updateStatus(UUID courseUuid, UUID threadUuid, ThreadRequest.UpdateStatus request) {
        return service.updateStatus(threadUuid, request);
    }

    @Override
    public ThreadResponse escalate(UUID courseUuid, UUID threadUuid, UUID escalatedByUuid) {
        return service.escalate(threadUuid, escalatedByUuid);
    }
}
