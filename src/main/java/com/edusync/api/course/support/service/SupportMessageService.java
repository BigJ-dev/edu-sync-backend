package com.edusync.api.course.support.service;

import com.edusync.api.course.support.dto.SupportMessageRequest;
import com.edusync.api.course.support.dto.SupportMessageResponse;
import com.edusync.api.course.support.enums.SenderType;

import java.util.List;
import java.util.UUID;

public interface SupportMessageService {

    SupportMessageResponse addMessage(UUID threadUuid, SupportMessageRequest.Create request);

    List<SupportMessageResponse> findAllByThread(UUID threadUuid);

    void markAsRead(UUID threadUuid, SenderType readerType, UUID readerUuid);
}
