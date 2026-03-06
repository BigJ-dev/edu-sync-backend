package com.edusync.api.course.focusmode.controller;

import com.edusync.api.course.focusmode.controller.api.FocusModeApi;
import com.edusync.api.course.focusmode.dto.FocusModeRequest;
import com.edusync.api.course.focusmode.dto.FocusModeResponse;
import com.edusync.api.course.focusmode.service.FocusModeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class FocusModeController implements FocusModeApi {

    private final FocusModeService service;

    @Override
    public FocusModeResponse activate(FocusModeRequest.Activate request) {
        return service.activate(request);
    }

    @Override
    public FocusModeResponse deactivate(UUID lecturerUuid, UUID courseUuid, UUID moduleUuid) {
        return service.deactivate(lecturerUuid, courseUuid, moduleUuid);
    }

    @Override
    public List<FocusModeResponse> findByLecturer(UUID lecturerUuid) {
        return service.findByLecturer(lecturerUuid);
    }

    @Override
    public List<FocusModeResponse> findActiveByCourse(UUID courseUuid) {
        return service.findActiveByCourse(courseUuid);
    }

    @Override
    public boolean isActive(UUID lecturerUuid, UUID courseUuid, UUID moduleUuid) {
        return service.isActive(lecturerUuid, courseUuid, moduleUuid);
    }
}
