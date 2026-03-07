package com.edusync.api.course.session.service;

import com.edusync.api.course.session.dto.ClassSessionRequest;
import com.edusync.api.course.session.dto.ClassSessionResponse;
import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import com.edusync.api.course.session.model.ClassSession;

import java.util.List;
import java.util.UUID;

public interface ClassSessionService {

    ClassSessionResponse createClassSession(UUID moduleUuid, ClassSessionRequest.Create request);

    List<ClassSessionResponse> findAllClassSessionsByModule(UUID moduleUuid, ClassSessionStatus status, ClassSessionType sessionType, String search);

    ClassSessionResponse findClassSessionByUuid(UUID sessionUuid);

    ClassSessionResponse updateClassSession(UUID sessionUuid, ClassSessionRequest.Update request);

    ClassSessionResponse updateClassSessionStatus(UUID sessionUuid, ClassSessionRequest.UpdateStatus request);

    ClassSession findClassSessionEntityByUuid(UUID uuid);
}
