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
    public StudentResponse createStudent(StudentRequest.Create request) {
        return service.createStudent(request);
    }

    @Override
    public Page<StudentResponse> findAllStudents(Boolean active, String search, Pageable pageable) {
        return service.findAllStudents(active, search, pageable);
    }

    @Override
    public StudentResponse findStudentByUuid(UUID uuid) {
        return service.findStudentByUuid(uuid);
    }

    @Override
    public StudentResponse updateStudent(UUID uuid, StudentRequest.Update request) {
        return service.updateStudent(uuid, request);
    }

    @Override
    public StudentResponse findStudentByCognitoSub(String cognitoSub) {
        return service.findStudentByCognitoSub(cognitoSub);
    }

    @Override
    public StudentResponse updateStudentProfileImage(UUID uuid, StudentRequest.ProfileImage request) {
        return service.updateStudentProfileImage(uuid, request);
    }
}
