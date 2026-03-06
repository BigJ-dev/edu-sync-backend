package com.edusync.api.course.attendance.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AttendanceField {

    CLASS_SESSION_ID("classSessionId"),
    STUDENT_ID("studentId"),
    COURSE_ID("courseId"),
    MODULE_ID("moduleId"),
    ATTENDANCE_STATUS("attendanceStatus"),
    SYNCED_FROM_TEAMS("syncedFromTeams"),
    MANUALLY_OVERRIDDEN("manuallyOverridden");

    private final String name;
}
