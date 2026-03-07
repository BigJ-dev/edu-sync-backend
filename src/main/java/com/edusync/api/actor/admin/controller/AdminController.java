package com.edusync.api.actor.admin.controller;

import com.edusync.api.actor.admin.controller.api.AdminApi;
import com.edusync.api.actor.admin.service.AdminService;
import com.edusync.api.actor.common.dto.AppUserRequest;
import com.edusync.api.actor.common.dto.AppUserResponse;
import com.edusync.api.actor.lecturer.service.LecturerService;
import com.edusync.api.actor.student.dto.StudentRequest;
import com.edusync.api.actor.student.dto.StudentResponse;
import com.edusync.api.actor.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class AdminController implements AdminApi {

    private final AdminService adminService;
    private final LecturerService lecturerService;
    private final StudentService studentService;

    @Override
    public AppUserResponse createAdmin(AppUserRequest.Create request) {
        return adminService.createAdmin(request);
    }

    @Override
    public Page<AppUserResponse> findAllAdmins(Boolean active, String search, Pageable pageable) {
        return adminService.findAllAdmins(active, search, pageable);
    }

    @Override
    public AppUserResponse findAdminByUuid(UUID uuid) {
        return adminService.findAdminByUuid(uuid);
    }

    @Override
    public AppUserResponse updateAdmin(UUID uuid, AppUserRequest.Update request) {
        return adminService.updateAdmin(uuid, request);
    }

    @Override
    public AppUserResponse findAdminByCognitoSub(String cognitoSub) {
        return adminService.findAdminByCognitoSub(cognitoSub);
    }

    @Override
    public AppUserResponse blockLecturer(UUID uuid, AppUserRequest.Block request) {
        return lecturerService.blockLecturer(uuid, request, 1L);
    }

    @Override
    public AppUserResponse unblockLecturer(UUID uuid) {
        return lecturerService.unblockLecturer(uuid);
    }

    @Override
    public StudentResponse blockStudent(UUID uuid, StudentRequest.Block request) {
        return studentService.blockStudent(uuid, request, 1L);
    }

    @Override
    public StudentResponse unblockStudent(UUID uuid) {
        return studentService.unblockStudent(uuid);
    }
}
