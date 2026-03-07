package com.edusync.api.course.attendance.service;

import com.edusync.api.course.attendance.dto.AttendanceRequest;
import com.edusync.api.course.attendance.dto.AttendanceResponse;
import com.edusync.api.course.attendance.enums.AttendanceStatus;

import java.util.List;
import java.util.UUID;

public interface AttendanceService {

    AttendanceResponse recordAttendance(UUID sessionUuid, AttendanceRequest.Record request);

    List<AttendanceResponse> findAllAttendanceBySession(UUID sessionUuid, AttendanceStatus status);

    AttendanceResponse overrideAttendance(UUID sessionUuid, UUID studentUuid, AttendanceRequest.Override request);
}
