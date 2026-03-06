package com.edusync.api.course.attendance.service;

import com.edusync.api.actor.common.model.AppUser;
import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.model.Student;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.attendance.dto.AttendanceRequest;
import com.edusync.api.course.attendance.dto.AttendanceResponse;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.model.AttendanceRecord;
import com.edusync.api.course.attendance.repo.AttendanceRepository;
import com.edusync.api.course.session.service.ClassSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository repository;
    private final ClassSessionService classSessionService;
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;

    public AttendanceResponse record(UUID sessionUuid, AttendanceRequest.Record request) {
        var session = classSessionService.findClassSession(sessionUuid);
        var student = findStudent(request.studentUuid());

        if (repository.existsByClassSessionIdAndStudentId(session.getId(), student.getId()))
            throw new ServiceException(HttpStatus.CONFLICT, "Attendance already recorded for this student in this session");

        var record = AttendanceRecord.builder()
                .classSession(session)
                .student(student)
                .course(session.getModule().getCourse())
                .module(session.getModule())
                .joinTime(request.joinTime())
                .leaveTime(request.leaveTime())
                .totalDurationMinutes(request.totalDurationMinutes())
                .attendanceStatus(request.attendanceStatus())
                .syncedFromTeams(false)
                .manuallyOverridden(false)
                .build();

        return AttendanceResponse.from(repository.save(record));
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> findAllBySession(UUID sessionUuid, AttendanceStatus status) {
        var session = classSessionService.findClassSession(sessionUuid);
        var spec = Specification.where(AttendanceSpec.hasSessionId(session.getId()))
                .and(AttendanceSpec.hasStatus(status));
        return repository.findAll(spec).stream().map(AttendanceResponse::from).toList();
    }

    public AttendanceResponse override(UUID sessionUuid, UUID studentUuid, AttendanceRequest.Override request) {
        var session = classSessionService.findClassSession(sessionUuid);
        var student = findStudent(studentUuid);
        var overrideBy = findAppUser(request.overrideByUuid());

        var record = repository.findByClassSessionIdAndStudentId(session.getId(), student.getId())
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Attendance record was not found"));

        record.setAttendanceStatus(request.attendanceStatus());
        record.setManuallyOverridden(true);
        record.setOverrideBy(overrideBy);
        record.setOverrideReason(request.overrideReason());
        return AttendanceResponse.from(repository.save(record));
    }

    private Student findStudent(UUID uuid) {
        return studentRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student was not found"));
    }

    private AppUser findAppUser(UUID uuid) {
        return appUserRepository.findByUuid(uuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "User was not found"));
    }
}
