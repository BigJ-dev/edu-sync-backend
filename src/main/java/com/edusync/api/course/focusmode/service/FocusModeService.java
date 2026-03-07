package com.edusync.api.course.focusmode.service;

import com.edusync.api.course.focusmode.dto.FocusModeRequest;
import com.edusync.api.course.focusmode.dto.FocusModeResponse;

import java.util.List;
import java.util.UUID;

public interface FocusModeService {

    FocusModeResponse activateFocusMode(FocusModeRequest.Activate request);

    FocusModeResponse deactivateFocusMode(UUID lecturerUuid, UUID courseUuid, UUID moduleUuid);

    List<FocusModeResponse> findFocusModesByLecturer(UUID lecturerUuid);

    List<FocusModeResponse> findActiveFocusModesByCourse(UUID courseUuid);

    boolean isFocusModeActive(UUID lecturerUuid, UUID courseUuid, UUID moduleUuid);
}
