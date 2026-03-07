package com.edusync.api.timetable.service;

import com.edusync.api.actor.common.repo.AppUserRepository;
import com.edusync.api.actor.student.repo.StudentRepository;
import com.edusync.api.common.exception.ServiceException;
import com.edusync.api.course.enrollment.enums.EnrollmentStatus;
import com.edusync.api.course.enrollment.repo.EnrollmentRepository;
import com.edusync.api.course.session.dto.ClassSessionRequest;
import com.edusync.api.course.session.repo.ClassSessionRepository;
import com.edusync.api.course.session.service.ClassSessionService;
import com.edusync.api.timetable.dto.TimetableEntry;
import com.edusync.api.timetable.dto.TimetableRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimetableServiceImpl implements TimetableService {

    private final ClassSessionRepository sessionRepository;
    private final ClassSessionService classSessionService;
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;

    @Override
    public List<TimetableEntry> getStudentTimetable(UUID studentUuid, LocalDate from, LocalDate to) {
        var student = studentRepository.findByUuid(studentUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Student not found"));

        var courseIds = enrollmentRepository.findByStudentIdAndStatus(student.getId(), EnrollmentStatus.ENROLLED)
                .stream()
                .map(e -> e.getCourse().getId())
                .toList();

        if (courseIds.isEmpty()) return List.of();

        var start = from.atStartOfDay().toInstant(ZoneOffset.UTC);
        var end = to.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        return sessionRepository.findByCourseIdInAndScheduledStartBetween(courseIds, start, end)
                .stream()
                .map(TimetableEntry::from)
                .toList();
    }

    @Override
    public List<TimetableEntry> getLecturerTimetable(UUID lecturerUuid, LocalDate from, LocalDate to) {
        var lecturer = appUserRepository.findByUuid(lecturerUuid)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "Lecturer not found"));

        var start = from.atStartOfDay().toInstant(ZoneOffset.UTC);
        var end = to.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        return sessionRepository.findByLecturerIdAndScheduledStartBetween(lecturer.getId(), start, end)
                .stream()
                .map(TimetableEntry::from)
                .toList();
    }

    @Override
    @Transactional
    public TimetableEntry createSession(TimetableRequest.CreateSession request) {
        var createRequest = new ClassSessionRequest.Create(
                request.title(),
                request.description(),
                request.sessionType(),
                request.sessionNumber(),
                request.scheduledStart(),
                request.scheduledEnd(),
                request.lecturerUuid(),
                request.groupUuid(),
                request.teamsMeetingId(),
                request.teamsJoinUrl(),
                request.venue()
        );

        var response = classSessionService.createClassSession(request.moduleUuid(), createRequest);
        var session = classSessionService.findClassSessionEntityByUuid(response.uuid());
        return TimetableEntry.from(session);
    }

    @Override
    @Transactional
    public TimetableEntry updateSession(UUID sessionUuid, TimetableRequest.UpdateSession request) {
        var updateRequest = new ClassSessionRequest.Update(
                request.title(),
                request.description(),
                request.sessionNumber(),
                request.scheduledStart(),
                request.scheduledEnd(),
                request.groupUuid(),
                request.teamsMeetingId(),
                request.teamsJoinUrl(),
                request.venue()
        );

        classSessionService.updateClassSession(sessionUuid, updateRequest);
        return TimetableEntry.from(classSessionService.findClassSessionEntityByUuid(sessionUuid));
    }

    @Override
    @Transactional
    public TimetableEntry updateSessionStatus(UUID sessionUuid, TimetableRequest.UpdateStatus request) {
        var statusRequest = new ClassSessionRequest.UpdateStatus(request.status());
        classSessionService.updateClassSessionStatus(sessionUuid, statusRequest);
        return TimetableEntry.from(classSessionService.findClassSessionEntityByUuid(sessionUuid));
    }
}
