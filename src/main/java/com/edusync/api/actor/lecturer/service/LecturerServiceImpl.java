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
public class LecturerServiceImpl implements LecturerService {

    private static final UserRole ROLE = UserRole.LECTURER;
    private final AppUserService appUserService;

    @Override
    public AppUserResponse createLecturer(AppUserRequest.Create request) {
        return appUserService.createAppUser(request, ROLE);
    }

    @Override
    public Page<AppUserResponse> findAllLecturers(Boolean active, String search, Pageable pageable) {
        return appUserService.findAllAppUsers(ROLE, active, search, pageable);
    }

    @Override
    public AppUserResponse findLecturerByUuid(UUID uuid) {
        return appUserService.findAppUserByUuid(uuid, ROLE);
    }

    @Override
    public AppUserResponse updateLecturer(UUID uuid, AppUserRequest.Update request) {
        return appUserService.updateAppUser(uuid, request, ROLE);
    }

    @Override
    public AppUserResponse blockLecturer(UUID uuid, AppUserRequest.Block request, Long blockedById) {
        return appUserService.blockAppUser(uuid, request, blockedById, ROLE);
    }

    @Override
    public AppUserResponse unblockLecturer(UUID uuid) {
        return appUserService.unblockAppUser(uuid, ROLE);
    }

    @Override
    public AppUserResponse findLecturerByCognitoSub(String cognitoSub) {
        return appUserService.findAppUserByCognitoSub(cognitoSub, ROLE);
    }
}
