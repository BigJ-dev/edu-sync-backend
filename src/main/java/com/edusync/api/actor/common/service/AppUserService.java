package com.edusync.api.actor.common.service;

import com.edusync.api.actor.common.dto.AppUserRequest;
import com.edusync.api.actor.common.dto.AppUserResponse;
import com.edusync.api.actor.common.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AppUserService {

    AppUserResponse createAppUser(AppUserRequest.Create request, UserRole role);

    Page<AppUserResponse> findAllAppUsers(UserRole role, Boolean active, String search, Pageable pageable);

    AppUserResponse findAppUserByUuid(UUID uuid, UserRole role);

    AppUserResponse updateAppUser(UUID uuid, AppUserRequest.Update request, UserRole role);

    AppUserResponse blockAppUser(UUID uuid, AppUserRequest.Block request, Long blockedById, UserRole role);

    AppUserResponse unblockAppUser(UUID uuid, UserRole role);

    AppUserResponse findAppUserByCognitoSub(String cognitoSub, UserRole role);
}
