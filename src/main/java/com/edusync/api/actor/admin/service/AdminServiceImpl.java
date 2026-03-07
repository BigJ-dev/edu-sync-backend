package com.edusync.api.actor.admin.service;

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
public class AdminServiceImpl implements AdminService {

    private static final UserRole ROLE = UserRole.ADMIN;
    private final AppUserService appUserService;

    @Override
    public AppUserResponse createAdmin(AppUserRequest.Create request) {
        return appUserService.createAppUser(request, ROLE);
    }

    @Override
    public Page<AppUserResponse> findAllAdmins(Boolean active, String search, Pageable pageable) {
        return appUserService.findAllAppUsers(ROLE, active, search, pageable);
    }

    @Override
    public AppUserResponse findAdminByUuid(UUID uuid) {
        return appUserService.findAppUserByUuid(uuid, ROLE);
    }

    @Override
    public AppUserResponse updateAdmin(UUID uuid, AppUserRequest.Update request) {
        return appUserService.updateAppUser(uuid, request, ROLE);
    }

    @Override
    public AppUserResponse findAdminByCognitoSub(String cognitoSub) {
        return appUserService.findAppUserByCognitoSub(cognitoSub, ROLE);
    }
}
