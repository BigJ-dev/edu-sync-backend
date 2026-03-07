package com.edusync.api.timetable.service;

import com.edusync.api.timetable.dto.TimetableEntry;
import com.edusync.api.timetable.dto.TimetableRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TimetableService {

    List<TimetableEntry> getStudentTimetable(UUID studentUuid, LocalDate from, LocalDate to);

    List<TimetableEntry> getLecturerTimetable(UUID lecturerUuid, LocalDate from, LocalDate to);

    TimetableEntry createSession(TimetableRequest.CreateSession request);

    TimetableEntry updateSession(UUID sessionUuid, TimetableRequest.UpdateSession request);

    TimetableEntry updateSessionStatus(UUID sessionUuid, TimetableRequest.UpdateStatus request);
}
