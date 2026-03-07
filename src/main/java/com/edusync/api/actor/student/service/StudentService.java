package com.edusync.api.actor.student.service;

import com.edusync.api.actor.student.dto.StudentRequest;
import com.edusync.api.actor.student.dto.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StudentService {

    StudentResponse createStudent(StudentRequest.Create request);

    Page<StudentResponse> findAllStudents(Boolean active, String search, Pageable pageable);

    StudentResponse findStudentByUuid(UUID uuid);

    StudentResponse updateStudent(UUID uuid, StudentRequest.Update request);

    StudentResponse blockStudent(UUID uuid, StudentRequest.Block request, Long blockedById);

    StudentResponse unblockStudent(UUID uuid);

    StudentResponse findStudentByCognitoSub(String cognitoSub);

    StudentResponse updateStudentProfileImage(UUID uuid, StudentRequest.ProfileImage request);
}
