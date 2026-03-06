package com.edusync.api.course.attendance.repo;

import com.edusync.api.course.attendance.model.AttendanceLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Long> {

    List<AttendanceLog> findByAttendanceRecordIdOrderByEventTimeAsc(Long attendanceRecordId);
}
