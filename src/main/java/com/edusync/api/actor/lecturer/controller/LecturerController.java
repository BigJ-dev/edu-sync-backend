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
    public AppUserResponse createLecturer(AppUserRequest.Create request) {
        return lecturerService.createLecturer(request);
    }

    @Override
    public Page<AppUserResponse> findAllLecturers(Boolean active, String search, Pageable pageable) {
        return lecturerService.findAllLecturers(active, search, pageable);
    }

    @Override
    public AppUserResponse findLecturerByUuid(UUID uuid) {
        return lecturerService.findLecturerByUuid(uuid);
    }

    @Override
    public AppUserResponse updateLecturer(UUID uuid, AppUserRequest.Update request) {
        return lecturerService.updateLecturer(uuid, request);
    }

    @Override
    public AppUserResponse findLecturerByCognitoSub(String cognitoSub) {
        return lecturerService.findLecturerByCognitoSub(cognitoSub);
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
