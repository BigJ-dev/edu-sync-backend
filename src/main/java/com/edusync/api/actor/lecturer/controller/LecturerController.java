package com.edusync.api.actor.lecturer.controller;

import com.edusync.api.actor.common.dto.AppUserRequest;
import com.edusync.api.actor.common.dto.AppUserResponse;
import com.edusync.api.actor.lecturer.controller.api.LecturerApi;
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
public class LecturerController implements LecturerApi {

    private final LecturerService lecturerService;
    private final StudentService studentService;

    @Override
    public AppUserResponse create(AppUserRequest.Create request) {
        return lecturerService.create(request);
    }

    @Override
    public Page<AppUserResponse> findAll(Boolean active, String search, Pageable pageable) {
        return lecturerService.findAll(active, search, pageable);
    }

    @Override
    public AppUserResponse findByUuid(UUID uuid) {
        return lecturerService.findByUuid(uuid);
    }

    @Override
    public AppUserResponse update(UUID uuid, AppUserRequest.Update request) {
        return lecturerService.update(uuid, request);
    }

    @Override
    public AppUserResponse findByCognitoSub(String cognitoSub) {
        return lecturerService.findByCognitoSub(cognitoSub);
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
