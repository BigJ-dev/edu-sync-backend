package com.edusync.api.teams.util;

import com.edusync.api.actor.student.model.Student;
import com.edusync.api.course.attendance.enums.AttendanceEvent;
import com.edusync.api.course.attendance.model.AttendanceRecord;
import com.edusync.api.course.attendance.repo.AttendanceLogRepository;
import com.edusync.api.course.attendance.repo.AttendanceRepository;
import com.edusync.api.teams.dto.AttendanceSyncContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.edusync.api.teams.util.TeamsAttendanceCalculator.*;

@Component
@RequiredArgsConstructor
public class TeamsAttendanceSyncUtil {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceLogRepository attendanceLogRepository;

    public Optional<AttendanceRecord> upsertAttendanceRecord(AttendanceSyncContext context, Student student, int durationMinutes) {
        var session = context.session();
        var existing = attendanceRepository.findByClassSessionIdAndStudentId(session.getId(), student.getId());

        if (existing.isPresent()) {
            return updateExistingRecord(existing.get(), context, durationMinutes);
        }

        return Optional.of(createNewRecord(context, student, durationMinutes));
    }

    public void saveAttendanceLogs(AttendanceRecord attendance, com.microsoft.graph.models.AttendanceRecord graphRecord) {
        Optional.ofNullable(graphRecord.getAttendanceIntervals())
                .ifPresent(intervals -> intervals.forEach(interval -> {
                    Optional.ofNullable(interval.getJoinDateTime()).ifPresent(joinTime ->
                            attendanceLogRepository.save(buildTeamsAttendanceLog(
                                    attendance, AttendanceEvent.JOIN,
                                    joinTime.toInstant(), graphRecord.getEmailAddress())));

                    Optional.ofNullable(interval.getLeaveDateTime()).ifPresent(leaveTime ->
                            attendanceLogRepository.save(buildTeamsAttendanceLog(
                                    attendance, AttendanceEvent.LEAVE,
                                    leaveTime.toInstant(), graphRecord.getEmailAddress())));
                }));
    }

    private Optional<AttendanceRecord> updateExistingRecord(
            AttendanceRecord record, AttendanceSyncContext context, int durationMinutes) {

        if (!record.isSyncedFromTeams() && record.isManuallyOverridden()) {
            return Optional.empty();
        }

        record.setTotalDurationMinutes(durationMinutes);
        record.setAttendanceStatus(calculateAttendanceStatusFromDuration(durationMinutes, context.session()));
        record.setSyncedFromTeams(true);
        record.setReportId(context.teamsReport().getId());

        return Optional.of(attendanceRepository.save(record));
    }

    private AttendanceRecord createNewRecord(AttendanceSyncContext context, Student student, int durationMinutes) {
        var session = context.session();

        var record = AttendanceRecord.builder()
                .classSession(session)
                .student(student)
                .course(session.getModule().getCourse())
                .module(session.getModule())
                .totalDurationMinutes(durationMinutes)
                .attendanceStatus(calculateAttendanceStatusFromDuration(durationMinutes, session))
                .syncedFromTeams(true)
                .manuallyOverridden(false)
                .reportId(context.teamsReport().getId())
                .build();

        return attendanceRepository.save(record);
    }
}
