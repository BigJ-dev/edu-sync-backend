package com.edusync.api.timetable.controller;

import com.edusync.api.timetable.controller.api.TimetableApi;
import com.edusync.api.timetable.dto.TimetableEntry;
import com.edusync.api.timetable.dto.TimetableRequest;
import com.edusync.api.timetable.service.TimetableService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TimetableController implements TimetableApi {

    private final TimetableService service;

    @Override
    public List<TimetableEntry> getStudentTimetable(UUID studentUuid, LocalDate from, LocalDate to) {
        var range = resolveRange(from, to);
        return service.getStudentTimetable(studentUuid, range[0], range[1]);
    }

    @Override
    public List<TimetableEntry> getLecturerTimetable(UUID lecturerUuid, LocalDate from, LocalDate to) {
        var range = resolveRange(from, to);
        return service.getLecturerTimetable(lecturerUuid, range[0], range[1]);
    }

    @Override
    public TimetableEntry createSession(TimetableRequest.CreateSession request) {
        return service.createSession(request);
    }

    @Override
    public TimetableEntry updateSession(UUID sessionUuid, TimetableRequest.UpdateSession request) {
        return service.updateSession(sessionUuid, request);
    }

    @Override
    public TimetableEntry updateSessionStatus(UUID sessionUuid, TimetableRequest.UpdateStatus request) {
        return service.updateSessionStatus(sessionUuid, request);
    }

    private LocalDate[] resolveRange(LocalDate from, LocalDate to) {
        var today = LocalDate.now();
        var start = Objects.nonNull(from) ? from : today.with(DayOfWeek.MONDAY);
        var end = Objects.nonNull(to) ? to : start.plusDays(6);
        return new LocalDate[]{start, end};
    }
}
