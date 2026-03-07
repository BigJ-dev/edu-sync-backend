package com.edusync.api.course.session.repo;

import com.edusync.api.course.session.model.ClassSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long>, JpaSpecificationExecutor<ClassSession> {

    Optional<ClassSession> findByUuid(UUID uuid);

    Optional<ClassSession> findByTeamsMeetingId(String teamsMeetingId);

    boolean existsByModuleIdAndSessionNumber(Long moduleId, int sessionNumber);

    @Query("SELECT cs FROM ClassSession cs WHERE cs.module.course.id = :courseId AND cs.scheduledStart >= :from AND cs.scheduledStart < :to")
    List<ClassSession> findByCourseIdAndScheduledStartBetween(Long courseId, Instant from, Instant to);

    @Query("SELECT cs FROM ClassSession cs WHERE cs.module.course.id IN :courseIds AND cs.scheduledStart >= :from AND cs.scheduledStart < :to ORDER BY cs.scheduledStart")
    List<ClassSession> findByCourseIdInAndScheduledStartBetween(List<Long> courseIds, Instant from, Instant to);

    @Query("SELECT cs FROM ClassSession cs WHERE cs.lecturer.id = :lecturerId AND cs.scheduledStart >= :from AND cs.scheduledStart < :to ORDER BY cs.scheduledStart")
    List<ClassSession> findByLecturerIdAndScheduledStartBetween(Long lecturerId, Instant from, Instant to);
}
