package com.edusync.api.actor.lecturer.service;

import com.edusync.api.actor.common.dto.AppUserRequest;
import com.edusync.api.actor.common.dto.AppUserResponse;
import com.edusync.api.actor.common.enums.UserRole;
import com.edusync.api.actor.common.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LecturerService {

    private static final UserRole ROLE = UserRole.LECTURER;
    private final AppUserService appUserService;

    public AppUserResponse create(AppUserRequest.Create request) {
        return appUserService.create(request, ROLE);
    }

    public Page<AppUserResponse> findAll(Boolean active, String search, Pageable pageable) {
        return appUserService.findAll(ROLE, active, search, pageable);
    }

    public AppUserResponse findByUuid(UUID uuid) {
        return appUserService.findByUuid(uuid, ROLE);
    }

    public AppUserResponse update(UUID uuid, AppUserRequest.Update request) {
        return appUserService.update(uuid, request, ROLE);
    }

    public AppUserResponse block(UUID uuid, AppUserRequest.Block request, Long blockedById) {
        return appUserService.block(uuid, request, blockedById, ROLE);
    }

    public AppUserResponse unblock(UUID uuid) {
        return appUserService.unblock(uuid, ROLE);
    }

    public AppUserResponse findByCognitoSub(String cognitoSub) {
        return appUserService.findByCognitoSub(cognitoSub, ROLE);
    }
}
