package com.edusync.api.course.messaging.controller;

import com.edusync.api.course.messaging.controller.api.MessageApi;
import com.edusync.api.course.messaging.dto.MessageRequest;
import com.edusync.api.course.messaging.dto.MessageResponse;
import com.edusync.api.course.messaging.enums.SenderType;
import com.edusync.api.course.messaging.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class MessageController implements MessageApi {

    private final MessageService service;

    @Override
    public MessageResponse create(UUID threadUuid, MessageRequest.Create request) {
        return service.addMessage(threadUuid, request);
    }

    @Override
    public List<MessageResponse> findByThread(UUID threadUuid) {
        return service.findByThread(threadUuid);
    }

    @Override
    public void markAsRead(UUID threadUuid, SenderType readerType, UUID readerUuid) {
        service.markAsRead(threadUuid, readerType, readerUuid);
    }
}
