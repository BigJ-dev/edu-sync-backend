package com.edusync.api.actor.student.service;

import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.actor.student.dto.StudentRequest;
import com.edusync.api.actor.student.dto.StudentResponse;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository repository;
    private final AppUserRepository appUserRepository;

    @Override
    public StudentResponse createStudent(StudentRequest.Create request) {
        validateUniqueness(request.cognitoSub(), request.email(), request.studentNumber());

        var student = Student.builder()
                .cognitoSub(request.cognitoSub())
                .studentNumber(request.studentNumber())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phone(request.phone())
                .active(true)
                .build();

        return StudentResponse.from(repository.save(student));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StudentResponse> findAllStudents(Boolean active, String search, Pageable pageable) {
        var spec = Stream.of(
                        StudentSpec.isActive(active),
                        StudentSpec.searchByNameEmailOrNumber(search))
                .filter(Objects::nonNull)
                .reduce(Specification::and)
                .orElse((root, query, cb) -> cb.conjunction());
        return repository.findAll(spec, pageable).map(StudentResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse findStudentByUuid(UUID uuid) {
        return StudentResponse.from(getStudentEntity(uuid));
    }

    @Override
    public StudentResponse updateStudent(UUID uuid, StudentRequest.Update request) {
        var student = getStudentEntity(uuid);
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setEmail(request.email());
        student.setPhone(request.phone());
        student.setStudentNumber(request.studentNumber());
        return StudentResponse.from(repository.save(student));
    }

    @Override
    public StudentResponse blockStudent(UUID uuid, StudentRequest.Block request, Long blockedById) {
        var student = getStudentEntity(uuid);
        var blocker = appUserRepository.findById(blockedById)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "The blocking user account was not found"));

        student.setActive(false);
        student.setBlockedAt(Instant.now());
        student.setBlockedBy(blocker);
        student.setBlockedReason(request.blockedReason());
        return StudentResponse.from(repository.save(student));
    }

    @Override
    public StudentResponse unblockStudent(UUID uuid) {
        var student = getStudentEntity(uuid);
        student.setActive(true);
        student.setBlockedAt(null);
        student.setBlockedBy(null);
        student.setBlockedReason(null);
        return StudentResponse.from(repository.save(student));
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponse findStudentByCognitoSub(String cognitoSub) {
        return repository.findByCognitoSub(cognitoSub)
                .map(StudentResponse::from)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student account was not found"));
    }

    @Override
    public StudentResponse updateStudentProfileImage(UUID uuid, StudentRequest.ProfileImage request) {
        var student = getStudentEntity(uuid);
        student.setProfileImageS3Key(request.profileImageS3Key());
        return StudentResponse.from(repository.save(student));
    }

    private void validateUniqueness(String cognitoSub, String email, String studentNumber) {
        if (repository.existsByCognitoSub(cognitoSub))
            throw new ServiceException(HttpStatus.CONFLICT, "An account with this identity already exists");
        if (repository.existsByEmail(email))
            throw new ServiceException(HttpStatus.CONFLICT, "This email address is already in use");
        if (repository.existsByStudentNumber(studentNumber))
            throw new ServiceException(HttpStatus.CONFLICT, "This student number is already registered");
    }

    private Student getStudentEntity(UUID uuid) {
        return repository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student account was not found"));
    }
}
