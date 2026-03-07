package com.edusync.api.course.messaging.service;

import com.edusync.api.course.messaging.dto.MessageRequest;
import com.edusync.api.course.messaging.dto.MessageResponse;
import com.edusync.api.course.messaging.enums.SenderType;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageResponse addMessageToThread(UUID threadUuid, MessageRequest.Create request);

    List<MessageResponse> findAllMessagesByThread(UUID threadUuid);

    void markMessagesAsRead(UUID threadUuid, SenderType readerType, UUID readerUuid);
}
