package com.edusync.api.actor.student.controller;

import com.edusync.api.actor.student.controller.api.StudentApi;
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
public class StudentController implements StudentApi {

    private final StudentService service;

    @Override
    public StudentResponse create(StudentRequest.Create request) {
        return service.create(request);
    }

    @Override
    public Page<StudentResponse> findAll(Boolean active, String search, Pageable pageable) {
        return service.findAll(active, search, pageable);
    }

    @Override
    public StudentResponse findByUuid(UUID uuid) {
        return service.findByUuid(uuid);
    }

    @Override
    public StudentResponse update(UUID uuid, StudentRequest.Update request) {
        return service.update(uuid, request);
    }

    @Override
    public StudentResponse findByCognitoSub(String cognitoSub) {
        return service.findByCognitoSub(cognitoSub);
    }

    @Override
    public StudentResponse updateProfileImage(UUID uuid, StudentRequest.ProfileImage request) {
        return service.updateProfileImage(uuid, request);
    }
}
