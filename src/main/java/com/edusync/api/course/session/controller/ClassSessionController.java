package com.edusync.api.course.session.controller;

import com.edusync.api.course.session.controller.api.ClassSessionApi;
import com.edusync.api.course.session.dto.ClassSessionRequest;
import com.edusync.api.course.session.dto.ClassSessionResponse;
import com.edusync.api.course.session.enums.ClassSessionStatus;
import com.edusync.api.course.session.enums.ClassSessionType;
import com.edusync.api.course.session.service.ClassSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class ClassSessionController implements ClassSessionApi {

    private final ClassSessionService service;

    @Override
    public ClassSessionResponse create(UUID moduleUuid, ClassSessionRequest.Create request) {
        return service.create(moduleUuid, request);
    }

    @Override
    public List<ClassSessionResponse> findAllByModule(UUID moduleUuid, ClassSessionStatus status, ClassSessionType sessionType, String search) {
        return service.findAllByModule(moduleUuid, status, sessionType, search);
    }

    @Override
    public ClassSessionResponse findByUuid(UUID moduleUuid, UUID classSessionUuid) {
        return service.findByUuid(classSessionUuid);
    }

    @Override
    public ClassSessionResponse update(UUID moduleUuid, UUID classSessionUuid, ClassSessionRequest.Update request) {
        return service.update(classSessionUuid, request);
    }

    @Override
    public ClassSessionResponse updateStatus(UUID moduleUuid, UUID classSessionUuid, ClassSessionRequest.UpdateStatus request) {
        return service.updateStatus(classSessionUuid, request);
    }
}
