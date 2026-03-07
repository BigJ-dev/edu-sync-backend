package com.edusync.api.actor.admin.service;

import com.edusync.api.actor.common.dto.AppUserRequest;
import com.edusync.api.actor.common.dto.AppUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminService {

    AppUserResponse createAdmin(AppUserRequest.Create request);

    Page<AppUserResponse> findAllAdmins(Boolean active, String search, Pageable pageable);

    AppUserResponse findAdminByUuid(UUID uuid);

    AppUserResponse updateAdmin(UUID uuid, AppUserRequest.Update request);

    AppUserResponse findAdminByCognitoSub(String cognitoSub);
}
