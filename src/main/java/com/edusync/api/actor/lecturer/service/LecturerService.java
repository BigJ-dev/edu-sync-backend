package com.edusync.api.actor.lecturer.service;

import com.edusync.api.actor.common.dto.AppUserRequest;
import com.edusync.api.actor.common.dto.AppUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface LecturerService {

    AppUserResponse createLecturer(AppUserRequest.Create request);

    Page<AppUserResponse> findAllLecturers(Boolean active, String search, Pageable pageable);

    AppUserResponse findLecturerByUuid(UUID uuid);

    AppUserResponse updateLecturer(UUID uuid, AppUserRequest.Update request);

    AppUserResponse blockLecturer(UUID uuid, AppUserRequest.Block request, Long blockedById);

    AppUserResponse unblockLecturer(UUID uuid);

    AppUserResponse findLecturerByCognitoSub(String cognitoSub);
}
