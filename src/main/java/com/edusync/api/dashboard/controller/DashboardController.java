package com.edusync.api.dashboard.controller;

import com.edusync.api.dashboard.controller.api.DashboardApi;
import com.edusync.api.dashboard.dto.*;
import com.edusync.api.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DashboardController implements DashboardApi {

    private final DashboardService service;

    @Override
    public AdminDashboard getAdminDashboard() {
        return service.getAdminDashboard();
    }

    @Override
    public LecturerDashboard getLecturerDashboard(UUID lecturerUuid) {
        return service.getLecturerDashboard(lecturerUuid);
    }

    @Override
    public StudentDashboard getStudentDashboard(UUID studentUuid) {
        return service.getStudentDashboard(studentUuid);
    }

    @Override
    public List<CourseAttendanceOverview> getStudentAttendance(UUID studentUuid) {
        return service.getStudentAttendance(studentUuid);
    }

    @Override
    public SessionAttendanceOverview getSessionAttendance(UUID sessionUuid) {
        return service.getSessionAttendance(sessionUuid);
    }
}
