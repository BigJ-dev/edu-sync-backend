package com.edusync.api.course.attendance.repo;

import com.edusync.api.course.attendance.model.AttendanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long>, JpaSpecificationExecutor<AttendanceRecord> {

    Optional<AttendanceRecord> findByClassSessionIdAndStudentId(Long classSessionId, Long studentId);

    boolean existsByClassSessionIdAndStudentId(Long classSessionId, Long studentId);

    List<AttendanceRecord> findByClassSessionId(Long classSessionId);

    List<AttendanceRecord> findByClassSessionIdIn(List<Long> classSessionIds);
}
