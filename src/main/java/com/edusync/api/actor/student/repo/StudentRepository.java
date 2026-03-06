package com.edusync.api.actor.student.repo;

import com.edusync.api.actor.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    Optional<Student> findByUuid(UUID uuid);

    Optional<Student> findByCognitoSub(String cognitoSub);

    boolean existsByEmail(String email);

    boolean existsByCognitoSub(String cognitoSub);

    boolean existsByStudentNumber(String studentNumber);
}
