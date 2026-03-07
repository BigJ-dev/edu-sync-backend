package com.edusync.api.dashboard.service;

import com.edusync.api.dashboard.dto.*;

import java.util.List;
import java.util.UUID;

public interface DashboardService {

    AdminDashboard getAdminDashboard();

    LecturerDashboard getLecturerDashboard(UUID lecturerUuid);

    StudentDashboard getStudentDashboard(UUID studentUuid);

    List<CourseAttendanceOverview> getStudentAttendance(UUID studentUuid);

    SessionAttendanceOverview getSessionAttendance(UUID sessionUuid);
}
