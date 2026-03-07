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
    public FocusModeResponse activateFocusMode(FocusModeRequest.Activate request) {
        return service.activateFocusMode(request);
    }

    @Override
    public FocusModeResponse deactivateFocusMode(UUID lecturerUuid, UUID courseUuid, UUID moduleUuid) {
        return service.deactivateFocusMode(lecturerUuid, courseUuid, moduleUuid);
    }

    @Override
    public List<FocusModeResponse> findFocusModesByLecturer(UUID lecturerUuid) {
        return service.findFocusModesByLecturer(lecturerUuid);
    }

    @Override
    public List<FocusModeResponse> findActiveFocusModesByCourse(UUID courseUuid) {
        return service.findActiveFocusModesByCourse(courseUuid);
    }

    @Override
    public boolean isFocusModeActive(UUID lecturerUuid, UUID courseUuid, UUID moduleUuid) {
        return service.isFocusModeActive(lecturerUuid, courseUuid, moduleUuid);
    }
}
