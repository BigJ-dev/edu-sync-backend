package com.edusync.api.course.messaging.service;

import com.edusync.api.course.messaging.dto.ThreadRequest;
import com.edusync.api.course.messaging.dto.ThreadResponse;
import com.edusync.api.course.messaging.enums.ThreadPriority;
import com.edusync.api.course.messaging.enums.ThreadStatus;
import com.edusync.api.course.messaging.model.MessageThread;

import java.util.List;
import java.util.UUID;

public interface MessageThreadService {

    ThreadResponse createMessageThread(ThreadRequest.Create request);

    List<ThreadResponse> findAllMessageThreads(UUID courseUuid, ThreadStatus status, ThreadPriority priority, Boolean escalated, String search);

    ThreadResponse findMessageThreadByUuid(UUID threadUuid);

    ThreadResponse updateMessageThreadStatus(UUID threadUuid, ThreadRequest.UpdateStatus request);

    ThreadResponse escalateMessageThread(UUID threadUuid, UUID escalatedByUuid);

    MessageThread findMessageThreadEntityByUuid(UUID uuid);
}
