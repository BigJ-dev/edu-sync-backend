package com.edusync.api.course.support.service;

import com.edusync.api.course.support.dto.SupportThreadRequest;
import com.edusync.api.course.support.dto.SupportThreadResponse;
import com.edusync.api.course.support.enums.ThreadPriority;
import com.edusync.api.course.support.enums.ThreadStatus;
import com.edusync.api.course.support.model.SupportThread;

import java.util.List;
import java.util.UUID;

public interface SupportThreadService {

    SupportThreadResponse createThread(SupportThreadRequest.Create request);

    List<SupportThreadResponse> findAllThreads(UUID courseUuid, ThreadStatus status, ThreadPriority priority, Boolean escalated, String search);

    SupportThreadResponse findThreadByUuid(UUID threadUuid);

    SupportThreadResponse updateThreadStatus(UUID threadUuid, SupportThreadRequest.UpdateStatus request);

    SupportThreadResponse escalateThread(UUID threadUuid, UUID escalatedByUuid);

    SupportThread findThreadEntityByUuid(UUID uuid);
}
