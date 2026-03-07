package com.edusync.api.course.attendance.controller;

import com.edusync.api.course.attendance.controller.api.AttendanceApi;
import com.edusync.api.course.attendance.dto.AttendanceRequest;
import com.edusync.api.course.attendance.dto.AttendanceResponse;
import com.edusync.api.course.attendance.enums.AttendanceStatus;
import com.edusync.api.course.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class AttendanceController implements AttendanceApi {

    private final AttendanceService service;

    @Override
    public AttendanceResponse record(UUID classSessionUuid, AttendanceRequest.Record request) {
        return service.recordAttendance(classSessionUuid, request);
    }

    @Override
    public List<AttendanceResponse> findAllBySession(UUID classSessionUuid, AttendanceStatus status) {
        return service.findAllAttendanceBySession(classSessionUuid, status);
    }

    @Override
    public AttendanceResponse override(UUID classSessionUuid, UUID studentUuid, AttendanceRequest.Override request) {
        return service.overrideAttendance(classSessionUuid, studentUuid, request);
    }
}
