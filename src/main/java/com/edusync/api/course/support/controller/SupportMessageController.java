package com.edusync.api.course.support.controller;

import com.edusync.api.course.support.controller.api.SupportMessageApi;
import com.edusync.api.course.support.dto.SupportMessageRequest;
import com.edusync.api.course.support.dto.SupportMessageResponse;
import com.edusync.api.course.support.enums.SenderType;
import com.edusync.api.course.support.service.SupportMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class SupportMessageController implements SupportMessageApi {

    private final SupportMessageService service;

    @Override
    public SupportMessageResponse create(UUID threadUuid, SupportMessageRequest.Create request) {
        return service.addMessage(threadUuid, request);
    }

    @Override
    public List<SupportMessageResponse> findByThread(UUID threadUuid) {
        return service.findAllByThread(threadUuid);
    }

    @Override
    public void markAsRead(UUID threadUuid, SenderType readerType, UUID readerUuid) {
        service.markAsRead(threadUuid, readerType, readerUuid);
    }
}
