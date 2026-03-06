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
    public AppUserResponse create(AppUserRequest.Create request) {
        return adminService.create(request);
    }

    @Override
    public Page<AppUserResponse> findAll(Boolean active, String search, Pageable pageable) {
        return adminService.findAll(active, search, pageable);
    }

    @Override
    public AppUserResponse findByUuid(UUID uuid) {
        return adminService.findByUuid(uuid);
    }

    @Override
    public AppUserResponse update(UUID uuid, AppUserRequest.Update request) {
        return adminService.update(uuid, request);
    }

    @Override
    public AppUserResponse findByCognitoSub(String cognitoSub) {
        return adminService.findByCognitoSub(cognitoSub);
    }

    @Override
    public AppUserResponse blockLecturer(UUID uuid, AppUserRequest.Block request) {
        return lecturerService.block(uuid, request, 1L);
    }

    @Override
    public AppUserResponse unblockLecturer(UUID uuid) {
        return lecturerService.unblock(uuid);
    }

    @Override
    public StudentResponse blockStudent(UUID uuid, StudentRequest.Block request) {
        return studentService.block(uuid, request, 1L);
    }

    @Override
    public StudentResponse unblockStudent(UUID uuid) {
        return studentService.unblock(uuid);
    }
}
