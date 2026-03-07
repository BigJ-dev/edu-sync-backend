package com.edusync.api.teams.dto;

import com.edusync.api.course.session.model.ClassSession;
import com.edusync.api.teams.model.TeamsAttendanceReport;

public record AttendanceSyncContext(
        ClassSession session,
        TeamsAttendanceReport teamsReport
) {}
