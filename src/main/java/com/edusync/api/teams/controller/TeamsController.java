package com.edusync.api.teams.controller;

import com.edusync.api.teams.controller.api.TeamsApi;
import com.edusync.api.teams.dto.TeamsRequest;
import com.edusync.api.teams.dto.TeamsResponse;
import com.edusync.api.teams.service.TeamsService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
public class TeamsController implements TeamsApi {

    private final TeamsService service;

    @Override
    public TeamsResponse.GuestInvite inviteStudentAsTeamsGuest(TeamsRequest.InviteGuest request) {
        return service.inviteStudentAsTeamsGuest(request);
    }

    @Override
    public TeamsResponse.MeetingCreated createTeamsOnlineClass(TeamsRequest.CreateMeeting request) {
        return service.createTeamsOnlineClass(request);
    }

    @Override
    public TeamsResponse.BulkInviteResult inviteStudentsForTeamsSession(TeamsRequest.BulkInvite request) {
        return service.inviteStudentsForTeamsSession(request);
    }

    @Override
    public List<TeamsResponse.SessionStudent> listStudentsForTeamsSession(UUID classSessionUuid) {
        return service.listStudentsForTeamsSession(classSessionUuid);
    }

    @Override
    public TeamsResponse.AttendanceSynced syncTeamsAttendance(TeamsRequest.SyncAttendance request) {
        return service.syncTeamsAttendance(request);
    }

    @Override
    public TeamsResponse.MonthlyReportResult generateTeamsMonthlyReport(TeamsRequest.MonthlyReport request) {
        return service.generateTeamsMonthlyReport(request);
    }
}
